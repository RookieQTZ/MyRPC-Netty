package com.zqt.rpc.util;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zqtstart
 * @create 2022-06-07 19:51
 */
@Slf4j
public class PackageScanUtil {

    // 获取 main 方法对应的类全类名
    public static String getStackTrace() {
        StackTraceElement[] stack = new Throwable().getStackTrace();
        return stack[stack.length - 1].getClassName();
    }

    /**
     * 获取当前路径下，所有类的集合
     * 默认扫描整个包
     *
     * @param packageDir 需要扫描的包的全路径，如  com/zqt/rpc/server/service
     * @return 当前路径下所有类的集合
     */
    public static Set<Class<?>> getClasses() {
        // 获取当前项目的 src 根路径
        String suffix = System.getProperty("user.dir") + "\\src\\main\\java\\";
        return getClasses(suffix);
    }

    public static Set<Class<?>> getClasses(String suffix) {

        Path path = Paths.get(suffix);
        Set<Class<?>> classes = new HashSet<>();

        // 把包之前的路径去掉，后缀名 .java 去掉，就是全路径名
        try {
            // path:遍历起点
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    try {
                        String filePath = file.toString();
                        if (filePath.endsWith(".java")) {
                            String className = filePath.split("\\.")[0]
                                    .substring(suffix.length())
                                    .replaceAll("\\\\", "\\.");// \\\\ 代表 \
//                            System.out.println(packageSuffix + className);
                            Class<?> clazz = Class.forName(className);
                            classes.add(clazz);
                        }
                    } catch (ClassNotFoundException e) {
                        log.error("扫面文件出错：", e);
                    }
                    return super.visitFile(file, attrs);
                }
            });
        } catch (Exception e) {
            log.error("扫面文件出错：", e);
        }

        return classes;
    }

}
