package com.zqt.rpc.client;

import com.zqt.rpc.client.manager.RpcClientManager;
import com.zqt.rpc.server.service.HelloLBWServiceImpl;
import com.zqt.rpc.server.service.HelloService;
import com.zqt.rpc.server.service.HelloServiceImpl;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zqtstart
 * @create 2022-06-06 17:04
 */
@Slf4j
public class RpcClientProxy {
    public static void main(String[] args) {
        // 需要用接口去获取容器对象。
//        HelloLBWServiceImpl service = RpcClientManager.getProxyService(HelloLBWServiceImpl.class);
        HelloService lbwService = RpcClientManager.getProxyService(HelloLBWServiceImpl.class);
        System.out.println(lbwService.sayHello("张三"));

        HelloService helloService = RpcClientManager.getProxyService(HelloServiceImpl.class);
        System.out.println(helloService.sayHello("李四"));
    }
}
