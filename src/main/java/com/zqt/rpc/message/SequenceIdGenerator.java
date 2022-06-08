package com.zqt.rpc.message;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zqtstart
 * @create 2022-06-06 18:11
 */
public class SequenceIdGenerator {
    static AtomicInteger id = new AtomicInteger(0);

    public static int nextId(){
        return id.getAndIncrement();
    }
}
