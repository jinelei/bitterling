package com.jinelei.bitterling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BitterlingApplication implements CommandLineRunner {
    private final Logger log = LoggerFactory.getLogger(BitterlingApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BitterlingApplication.class, args);
    }

    @Override
    public void run(String... args) {
        log.warn("系统启动成功!");
    }
}
