package com.couple.space;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 应用程序入口类
 * 使用@SpringBootApplication注解标记这是一个Spring Boot应用
 */
@SpringBootApplication
public class SpaceApplication {
    /**
     * 应用程序入口方法
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(SpaceApplication.class, args);
    }
} 