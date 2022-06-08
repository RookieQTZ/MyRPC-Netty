package com.zqt.rpc.client.loadbalance;

import com.alibaba.nacos.api.naming.pojo.Instance;

import java.util.List;
import java.util.Random;

/**
 * @author zqtstart
 * @create 2022-06-08 0:59
 */
public class RandomLoadBalancer implements LoadBalancer {
    @Override
    public Instance getInstance(List<Instance> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}
