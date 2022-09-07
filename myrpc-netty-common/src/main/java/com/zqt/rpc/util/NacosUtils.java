package com.zqt.rpc.util;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * 负责服务的管理（注册、获取、注销）
 *
 * @author zqtstart
 * @create 2022-06-07 23:39
 */
@Slf4j
public class NacosUtils {
    private static NamingService namingService;

    private static final String SERVER_ADDR = "127.0.0.1:8848";

    static {
        try {
            namingService = NamingFactory.createNamingService(SERVER_ADDR);
        } catch (NacosException e) {
            log.error("连接到 nacos 出现异常", e);
        }
    }

    /**
     * 服务注册
     *
     * @param serverName
     * @param inetSocketAddress
     */
    public static void register(String serverName, InetSocketAddress inetSocketAddress) {
        try {
            namingService.registerInstance(serverName, inetSocketAddress.getHostName(), inetSocketAddress.getPort());
        } catch (NacosException e) {
            log.error("注册服务时发生错误", e);
        }
    }

    /**
     * 获取当前服务名中的所有实例
     *
     * @return
     */
    public static List<Instance> getAllInstances(String serverName) throws NacosException {
        return namingService.getAllInstances(serverName);
    }

    /**
     * 注销服务
     */
}
