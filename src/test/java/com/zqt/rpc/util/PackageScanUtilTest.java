package com.zqt.rpc.util;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * @author zqtstart
 * @create 2022-06-07 20:15
 */
public class PackageScanUtilTest {

    @Test
    public void getClasses() {
        Set<Class<?>> classes = PackageScanUtil.getClasses();
        System.out.println(classes.size());
        for (Class<?> clazz : classes) {
            System.out.println(clazz);
        }
    }

    @Test
    public void mainClassPath(){
        StackTraceElement[] stack = new Throwable().getStackTrace();
        System.out.println(stack[stack.length - 1].getClassName());
    }

    @Test
    public void testPath(){
        String suffix = System.getProperty("user.dir") + "\\src\\main\\java\\";
        Path path = Paths.get(suffix);
        System.out.println(path);
    }

}