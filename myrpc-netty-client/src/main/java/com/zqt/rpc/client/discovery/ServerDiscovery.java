package com.zqt.rpc.client.discovery;

import java.net.InetSocketAddress;

/**
 * @author zqtstart
 * @create 2022-06-08 0:51
 */
public interface ServerDiscovery {
    InetSocketAddress getService(String serverName) throws Exception;
}
