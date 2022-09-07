package com.zqt.rpc.client.handler;


import com.zqt.rpc.message.RpcResponseMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zqtstart
 * @create 2022-06-06 17:03
 */
@Slf4j
@ChannelHandler.Sharable
public class RpcResponseMessageHandler extends SimpleChannelInboundHandler<RpcResponseMessage> {

    // 用来存放接收结果的 promise
    public static final Map<Integer, Promise<Object>> PROMISES = new ConcurrentHashMap<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponseMessage msg) throws Exception {
        Promise<Object> promise = PROMISES.remove(msg.getSequenceId());
        if(promise != null) {
            Object returnValue = msg.getReturnValue();
            Exception exception = msg.getException();
            if (msg.getReturnValue() != null) {
                promise.setSuccess(returnValue);
                log.debug("return value: {}", msg.getReturnValue());
            } else if (msg.getException() != null) {
                promise.setFailure(exception);
                log.error("exception: {}", msg.getException().getCause().getMessage());
            }
        }
    }
}
