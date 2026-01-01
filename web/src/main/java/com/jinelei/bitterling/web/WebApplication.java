package com.jinelei.bitterling.web;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.URL;
import java.util.Enumeration;

@SpringBootApplication
//@Import(GlobalExceptionHandler.class)
public class WebApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 查找classpath中所有的data.sql
        Enumeration<URL> resources = getClass().getClassLoader().getResources("database/data.sql");
        if (resources.hasMoreElements()) {
            System.out.println("找到data.sql，路径：" + resources.nextElement());
        } else {
            System.out.println("未找到data.sql！当前classpath包含：");
            // 打印所有classpath路径，确认resources是否在其中
            Enumeration<URL> allResources = getClass().getClassLoader().getResources("");
            while (allResources.hasMoreElements()) {
                System.out.println(allResources.nextElement());
            }
        }
    }
}
