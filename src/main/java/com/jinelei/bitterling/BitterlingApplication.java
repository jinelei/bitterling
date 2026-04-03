package com.jinelei.bitterling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class BitterlingApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(BitterlingApplication.class);
    private final Environment environment;

    public BitterlingApplication(Environment environment) {
        this.environment = environment;
    }

    public static void main(String[] args) {
        SpringApplication.run(BitterlingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.warn("系统启动成功! >>> {}:{}", environment.getProperty("server.address", "0.0.0.0"), environment.getProperty("server.port", "8080"));
    }
}
