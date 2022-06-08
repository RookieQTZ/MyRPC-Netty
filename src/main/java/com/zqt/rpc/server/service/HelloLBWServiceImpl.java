package com.zqt.rpc.server.service;

import com.zqt.rpc.server.annotatios.RpcServer;

/**
 * @author zqtstart
 * @create 2022-06-06 12:30
 */
@RpcServer
public class HelloLBWServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "卢本伟说： Hello " + name + "!?!?";
    }
}
