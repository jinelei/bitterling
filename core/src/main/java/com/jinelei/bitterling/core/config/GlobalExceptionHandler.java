package com.jinelei.bitterling.core.config;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jinelei.bitterling.core.domain.response.GenericResult;
import com.jinelei.bitterling.core.helper.ThrowableHelper;

@RestControllerAdvice
@Order(Integer.MAX_VALUE)
public class GlobalExceptionHandler extends BaseExceptionHandler {

    /**
     * 处理所有未捕获的未知异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public GenericResult<?> handleGlobalException(Exception e) {
        // 打印异常栈信息，方便排查问题
        log.error("全局捕获未知异常: {}", ThrowableHelper.getStackTraceAsString(e));
        // 生产环境建议返回通用提示，避免暴露敏感信息
        return GenericResult.of(500, "系统繁忙，请稍后再试");
    }

}