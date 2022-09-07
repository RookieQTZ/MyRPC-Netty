package com.zqt.rpc.message;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zqtstart
 * @create 2022-06-03 20:25
 */
@Data
public abstract class Message implements Serializable {

    // 根据消息类型，获取对应消息类
    public static Class<?> getMessageClass(int messageType){
        return messageClasses.get(messageType);
    }

    public abstract int getMessageType();

    // 保证全局唯一
    private int sequenceId;

    public static final int PingMessage = 14;
    public static final int PongMessage = 15;
    public static final int RpcRequestMessage = 101;
    public static final int RpcResponseMessage = 102;

    // 通过消息的类型，获取对应的 Class 对象
    private static Map<Integer, Class<?>> messageClasses = new HashMap<>();
    static {
        messageClasses.put(RpcRequestMessage, RpcRequestMessage.class);
        messageClasses.put(RpcResponseMessage, RpcResponseMessage.class);
    }

}
