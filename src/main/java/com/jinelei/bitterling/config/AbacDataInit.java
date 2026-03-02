package com.jinelei.bitterling.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

// 项目启动时初始化测试数据
@Component
@RequiredArgsConstructor
public class AbacDataInit implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        // 1. 初始化会话+用户属性
        String sessionId = "test_session_123456";
        redisTemplate.opsForHash().putAll("session:test_session_123456",
                Map.of("userId", "1001", "role", "admin", "dept", "tech", "device", "pc"));
        redisTemplate.expire("session:test_session_123456", 2, TimeUnit.HOURS);

        // 2. 初始化用户扩展属性
        redisTemplate.opsForHash().putAll("user:attr:1001",
                Map.of("job", "dev", "level", "P3", "tempAuth", "[\"doc_100\",\"doc_101\"]"));

        // 3. 初始化资源属性
        redisTemplate.opsForHash().putAll("resource:attr:/api/doc/100",
                Map.of("type", "document", "owner", "1001", "dept", "tech", "level", "secret"));

        // 4. 初始化IP白名单
        redisTemplate.opsForSet().add("env:ip_white_list", "127.0.0.1", "192.168.1.100");
    }
}