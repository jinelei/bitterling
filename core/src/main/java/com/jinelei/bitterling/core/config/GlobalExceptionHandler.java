package com.jinelei.bitterling.core.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.core.utils.ThrowableUtils;

@RestControllerAdvice
@Order(Integer.MAX_VALUE)
public class GlobalExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理所有未捕获的未知异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public GenericResult<String> handleGlobalException(Exception e) {
        log.error("全局捕获未知异常: {}", ThrowableUtils.getStackTraceAsString(e));
        return GenericResult.failure(e.getMessage());
    }

}
