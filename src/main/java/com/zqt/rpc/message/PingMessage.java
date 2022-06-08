package com.zqt.rpc.message;

/**
 * @author zqtstart
 * @create 2022-06-06 0:19
 */
public class PingMessage extends Message {
    @Override
    public int getMessageType() {
        return PingMessage;
    }
}
