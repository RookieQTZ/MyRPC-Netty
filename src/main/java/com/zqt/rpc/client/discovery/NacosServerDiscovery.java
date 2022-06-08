package com.zqt.rpc.client.discovery;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.zqt.rpc.client.loadbalance.LoadBalancer;
import com.zqt.rpc.client.loadbalance.RandomLoadBalancer;
import com.zqt.rpc.util.NacosUtils;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author zqtstart
 * @create 2022-06-08 0:52
 */
@Slf4j
public class NacosServerDiscovery implements ServerDiscovery {

    // 负载均衡算法不能随意改变
    private final LoadBalancer loadBalancer = new RandomLoadBalancer();

    @Override
    public InetSocketAddress getService(String serverName) throws NacosException {
        List<Instance> services = NacosUtils.getAllInstances(serverName);
        if (services.size() == 0) {
            // e
            throw new RuntimeException("找不到对应服务");
        }
        Instance instance = loadBalancer.getInstance(services);
        return new InetSocketAddress(instance.getIp(), instance.getPort());
    }
}
