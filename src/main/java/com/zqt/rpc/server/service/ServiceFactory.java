package com.zqt.rpc.server.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zqtstart
 * @create 2022-06-07 21:39
 */
public class ServiceFactory {

    // 存放提供的服务的对应的实例
    public static final Map<String, Object> services = new ConcurrentHashMap<>();

    public <T> void addService(String serverName, T server){
        services.put(serverName, server);
    }

}
