package com.zqt.rpc.server.service;

import java.io.InputStream;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 模拟注册中心，提供 服务端提供的服务
 *
 * @author zqtstart
 * @create 2022-06-06 13:43
 */
public class RpcServicesFactory {
    private static Properties properties;
    private static ConcurrentHashMap<Class<?>, Object> map = new ConcurrentHashMap<>();

    static {
        // 为什么不加 / 不行？？
        // 不行， / 表示根目录，如果不加表示当前路径
        try (InputStream is = RpcServicesFactory.class.getResourceAsStream("/application.properties")) {

            properties = new Properties();
            properties.load(is);
            Set<String> names = properties.stringPropertyNames();
            for (String name : names) {
                if(name.endsWith("Service")){
                    Class<?> interfaceClass = Class.forName(name);
                    Class<?> instanceClass = Class.forName(properties.getProperty(name));
                    map.put(interfaceClass, instanceClass.newInstance());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务接口实例
     * @param interfaceClass 调用接口服务
     * @return 没有当前服务，返回 null
     */
    public static <T> T getService(Class<T> interfaceClass){
        return (T) map.getOrDefault(interfaceClass, null);
    }
}
