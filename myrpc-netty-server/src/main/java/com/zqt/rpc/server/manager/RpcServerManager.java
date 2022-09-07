package com.zqt.rpc.server.manager;

import com.zqt.rpc.protocol.MessageCodecSharable;
import com.zqt.rpc.protocol.ProtocolFrameDecoder;
import com.zqt.rpc.server.handler.RpcRequestMessageHandler;
import com.zqt.rpc.service.ServiceFactory;
import com.zqt.rpc.util.NacosUtils;
import com.zqt.rpc.util.PackageScanUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import com.zqt.rpc.annotatios.RpcServer;

import java.net.InetSocketAddress;

/**
 * 负责自动注册服务，启动服务端
 *
 * @author zqtstart
 * @create 2022-06-07 21:19
 */
@Slf4j
public class RpcServerManager {

    // 需要注册的服务所在的包
    // 如：com/zqt/rpc/server/service
    public String servicePackage;
    private ServiceFactory serviceFactory;
    protected String ip;
    protected int port;

    public RpcServerManager() {
        this("127.0.0.1", 8080, "com\\zqt\\rpc\\server\\service");
    }

    public RpcServerManager(String ip, int port, String servicePackage) {
        this.serviceFactory = new ServiceFactory();
        this.servicePackage = servicePackage;
        this.ip = ip;
        this.port = port;
        autoRegistry();
    }

    public void start(){
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        RpcRequestMessageHandler RPC_REQUEST_HANDLER = new RpcRequestMessageHandler();
        try {
            Channel channel = new ServerBootstrap()
                    .group(boss, worker)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        // SocketChannel 才是负责客户端通信的 channel
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ProtocolFrameDecoder());
                            ch.pipeline().addLast(LOGGING_HANDLER);
                            ch.pipeline().addLast(MESSAGE_CODEC);
                            ch.pipeline().addLast(RPC_REQUEST_HANDLER);
                        }
                    })
                    .bind(new InetSocketAddress(8080)).sync().channel();
            channel.closeFuture().sync();// 同步等待关闭
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

    /**
     * 自动注册服务
     * 将带有 RpcServer 注解的类的实例，放入 factory 中
     */
    private void autoRegistry(){
        // todo：不要把路径写死
        String suffix = System.getProperty("user.dir") + "\\myrpc-netty-server" + "\\src\\main\\java\\";
        for (Class<?> clazz : PackageScanUtil.getClasses(suffix)) {
            if (clazz.isAnnotationPresent(RpcServer.class)) {
                String serverName = clazz.getName();
                Object instance;
                try {
                    instance = clazz.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("创建对象 {} 发生错误", clazz);
                    continue;
                }
                addServer(serverName, instance);
            }
        }

    }

    /**
     * 添加服务对象到工厂和注册到注册中心
     * @param serverName
     * @param server
     */
    private <T> void addServer(String serverName, T server){
        serviceFactory.addService(serverName, server);
        NacosUtils.register(serverName, new InetSocketAddress(ip, port));
        log.debug("注册服务" + serverName);
    }
}
