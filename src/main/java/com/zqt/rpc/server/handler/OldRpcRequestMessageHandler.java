package com.zqt.rpc.server.handler;

import com.zqt.rpc.message.RpcRequestMessage;
import com.zqt.rpc.message.RpcResponseMessage;
import com.zqt.rpc.server.service.HelloService;
import com.zqt.rpc.server.service.RpcServicesFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * @author zqtstart
 * @create 2022-06-06 13:42
 */
@Slf4j
@ChannelHandler.Sharable
@Deprecated
public class OldRpcRequestMessageHandler extends SimpleChannelInboundHandler<RpcRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequestMessage message) throws Exception {
        /*RpcResponseMessage responseMessage = new RpcResponseMessage();
        // 加上消息序列号
        responseMessage.setSequenceId(message.getSequenceId());
        // 考虑出现异常的情况
        try {
            // 需要根据客户端的 request 请求来调用服务方法
            HelloService service = (HelloService) RpcServicesFactory.getService(Class.forName(message.getInterfaceImplName()));
            Method method = service.getClass().getMethod(message.getMethodName(), message.getParameterType());
            Object invoke = method.invoke(service, message.getParameterValue());
            responseMessage.setReturnValue(invoke);
        } catch (Exception e) {
            e.printStackTrace();
            responseMessage.setException(e);
        }
        ctx.writeAndFlush(responseMessage);*/
    }
}
