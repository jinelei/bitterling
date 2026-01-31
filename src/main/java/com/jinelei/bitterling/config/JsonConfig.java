package com.jinelei.bitterling.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * json配置
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2026-02-01
 */
@Configuration
public class JsonConfig {
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 常用配置：支持 JDK8 时间类型、忽略空值、统一日期格式
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        return objectMapper;
    }
}
