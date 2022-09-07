package com.zqt.rpc.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zqtstart
 * @create 2022-06-06 12:14
 */
@Data
@ToString(callSuper = true)
public class RpcResponseMessage extends Message {

    /**
     * 返回值
     */
    private Object returnValue;

    /**
     * 异常值
     */
    private Exception exception;

    @Override
    public int getMessageType() {
        return RpcResponseMessage;
    }
}
