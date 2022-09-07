package com.zqt.rpc.client.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;

/**
 * 服务负载均衡
 *
 * @author zqtstart
 * @create 2022-06-08 0:54
 */
public interface LoadBalancer {
    Instance getInstance(List<Instance> list);
}
