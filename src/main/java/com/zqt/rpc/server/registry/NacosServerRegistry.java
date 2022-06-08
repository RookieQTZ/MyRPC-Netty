package com.zqt.rpc.server.registry;

import com.zqt.rpc.util.NacosUtils;

import java.net.InetSocketAddress;

/**
 * @author zqtstart
 * @create 2022-06-07 23:37
 */
public class NacosServerRegistry implements ServerRegistry{
    @Override
    public void register(String serverName, InetSocketAddress inetSocketAddress) {
        NacosUtils.register(serverName, inetSocketAddress);
    }
}
