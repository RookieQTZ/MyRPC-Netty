package com.zqt.rpc.server;

import com.zqt.rpc.server.manager.RpcServerManager;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zqtstart
 * @create 2022-06-06 12:31
 */
@Slf4j
public class RpcServer {
    public static void main(String[] args) {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        System.out.println(stack[stack.length - 1].getClassName());
        new RpcServerManager().start();
    }
}
