package com.zqt.rpc.message;

/**
 * @author zqtstart
 * @create 2022-06-06 0:20
 */
public class PongMessage extends Message{
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
