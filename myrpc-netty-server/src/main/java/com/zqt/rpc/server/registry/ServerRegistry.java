package com.zqt.rpc.server.registry;

import java.net.InetSocketAddress;

/**
 * @author zqtstart
 * @create 2022-06-07 23:35
 */
public interface ServerRegistry {
    /**
     * 将服务的名字和地址注册进注册中心
     * @param serverName
     * @param inetSocketAddress
     */
    void register(String serverName, InetSocketAddress inetSocketAddress);
}
