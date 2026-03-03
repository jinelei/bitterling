package com.jinelei.bitterling.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AbacDataInit implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        // 1. 初始化会话+用户属性
        String username = "jinelei";
        // 3. 初始化资源属性
        redisTemplate.opsForHash().putAll("resource:attr:/memo",
                Map.of("type", "document", "owner", username, "securityLevel", "secret"));

        // 4. 初始化IP白名单
        redisTemplate.opsForSet().add("env:ip_white_list", "127.0.0.1", "192.168.3.0/24");
    }
}