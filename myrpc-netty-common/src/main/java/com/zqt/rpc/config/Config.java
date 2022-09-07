package com.zqt.rpc.config;

import com.zqt.rpc.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author zqtstart
 * @create 2022-09-06 13:05
 */
public abstract class Config {
    static Properties properties;

    static {
        // 类路径下获取资源
        try(InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Serializer.Algorithm getSerializerAlgorithm(){
        String algorithm = properties.getProperty("serializer.algorithm");
        if(algorithm == null){
            return Serializer.Algorithm.java;
        }else{
            return Serializer.Algorithm.valueOf(algorithm);
        }
    }
}
