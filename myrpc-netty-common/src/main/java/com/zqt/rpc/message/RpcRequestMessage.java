package com.zqt.rpc.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author zqtstart
 * @create 2022-06-06 12:14
 */
@Data
@ToString(callSuper = true)
public class RpcRequestMessage extends Message {

    /**
     * 调用提供的服务类的全限定名
     */
    private String serverName;

    /**
     * 调用接口中的方法名
     */
    private String methodName;

    /**
     * 调用方法的返回值类型
     */
    private Class<?> returnType;

    /**
     * 方法参数类型数组
     */
    private Class<?>[] parameterType;

    /**
     * 方法参数数值数组
     */
    private Object[] parameterValue;

    public RpcRequestMessage(int sequenceId,
                             String serverName,
                             String methodName,
                             Class<?> returnType,
                             Class<?>[] parameterType,
                             Object[] parameterValue) {
        super.setSequenceId(sequenceId);
        this.serverName = serverName;
        this.methodName = methodName;
        this.returnType = returnType;
        this.parameterType = parameterType;
        this.parameterValue = parameterValue;
    }

    @Override
    public int getMessageType() {
        return RpcRequestMessage;
    }
}
