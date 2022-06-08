package com.zqt.rpc.client.manager;

import com.zqt.rpc.client.discovery.NacosServerDiscovery;
import com.zqt.rpc.client.discovery.ServerDiscovery;
import com.zqt.rpc.client.handler.RpcResponseMessageHandler;
import com.zqt.rpc.message.RpcRequestMessage;
import com.zqt.rpc.message.SequenceIdGenerator;
import com.zqt.rpc.protocol.MessageCodecSharable;
import com.zqt.rpc.protocol.ProtocolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;

/**
 * @author zqtstart
 * @create 2022-06-08 0:40
 */
@Slf4j
public class RpcClientManager {

    private static final ServerDiscovery serverDiscovery = new NacosServerDiscovery();

    public static <T> T getProxyService(Class<T> serviceClass){

        // 发现服务
        // 连接服务对应的主机
        InetSocketAddress inetSocketAddress = null;
        try {
            inetSocketAddress = serverDiscovery.getService(serviceClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
            log.debug("获取服务地址失败", e);
        }
        Channel channel = getChannel(inetSocketAddress);

        ClassLoader classLoader = serviceClass.getClassLoader();
        Class<?>[] interfaces = serviceClass.getInterfaces();// ??? 泛型

        // 生成的代理对象
        Object o = Proxy.newProxyInstance(classLoader, interfaces, new InvocationHandler() {
            @Override
            //                                  sayHello       "zhangsan"
            // 获取的代理对象执行方法时，才会调用 invoke
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                RpcRequestMessage message = new RpcRequestMessage(
                        SequenceIdGenerator.nextId(),
                        serviceClass.getName(),
                        method.getName(),
                        method.getReturnType(),
                        method.getParameterTypes(),
                        args
                );

                // 发送消息
                channel.writeAndFlush(message);

                // 等待返回结果
                Promise<Object> promise = new DefaultPromise<>(channel.eventLoop());
                RpcResponseMessageHandler.PROMISES.put(message.getSequenceId(), promise);

                //
                promise.await();
                if(promise.isSuccess()){
                    // 调用成功
                    return promise.getNow();
                }else{
                    // 调用失败
                    throw new RuntimeException(promise.cause());
                }
            }
        });
        return (T) o;
    }

    private static Channel channel;
    private static final Object lock = new Object();

    // 线程安全单例模式，客户端多个线程只能拿到一个 channel
    private static Channel getChannel(InetSocketAddress inetSocketAddress){
        if(channel != null){
            return channel;
        }
        // t1 t2
        synchronized (lock){
            if(channel != null){
                return channel;
            }
            initChannel(inetSocketAddress);
        }
        return channel;
    }

    private static void initChannel(InetSocketAddress inetSocketAddress){
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcResponseMessageHandler RPC_RESPONSE_HANDLER = new RpcResponseMessageHandler();
        try {
            channel = new Bootstrap()
                    .group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(RPC_RESPONSE_HANDLER);
                        }
                    })
                    .connect(inetSocketAddress)
                    .sync()
                    .channel();

            ChannelFuture channelFuture = channel.closeFuture();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    group.shutdownGracefully();
                }
            });

        } catch (InterruptedException e) {
            log.error("client error", e);
        }
    }

    public static void main(String[] args) {
        getChannel(new InetSocketAddress("localhost", 8080));
    }
}
