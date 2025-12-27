package com.jinelei.bitterling.core.config.auto;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jinelei.bitterling.core.config.GlobalExceptionHandler;

/**
 * 外部包的自动配置类
 */
@Configuration
public class ExceptionAutoConfiguration {

    // 将 GlobalExceptionHandler 注册为 Spring Bean
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
    
}
