package com.zqt.rpc.server.service;

import com.zqt.rpc.annotatios.RpcServer;
import com.zqt.rpc.api.service.HelloService;

/**
 * @author zqtstart
 * @create 2022-09-07 1:16
 */
@RpcServer
public class HelloZQTServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "zqt say:" + name + ", hello!?";
    }
}
