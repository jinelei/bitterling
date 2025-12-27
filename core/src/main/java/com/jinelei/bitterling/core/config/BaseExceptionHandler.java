package com.jinelei.bitterling.core.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jinelei.bitterling.core.domain.response.GenericResult;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.helper.ThrowableHelper;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 */
public abstract class BaseExceptionHandler {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public GenericResult<?> handleBusinessException(BusinessException e) {
        log.debug("全局捕获业务异常: {}", ThrowableHelper.getStackTraceAsString(e));
        return GenericResult.of(e.getCode(), e.getMessage());
    }

    /**
     * 处理请求参数校验异常（@RequestBody 注解的参数）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResult<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("全局捕获校验异常: {}", ThrowableHelper.getStackTraceAsString(e));
        // 拼接所有字段的错误信息
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return GenericResult.of(400, "参数校验失败：" + errorMsg);
    }

    /**
     * 处理请求参数绑定异常（如参数类型不匹配）
     */
    @ExceptionHandler(BindException.class)
    public GenericResult<?> handleBindException(BindException e) {
        log.debug("全局捕获绑定异常: {}", ThrowableHelper.getStackTraceAsString(e));
        String errorMsg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return GenericResult.of(400, "参数绑定失败：" + errorMsg);
    }

    /**
     * 处理请求体解析异常（如JSON格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public GenericResult<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.debug("全局捕获解析异常: {}", ThrowableHelper.getStackTraceAsString(e));
        return GenericResult.of(400, "请求体解析失败：请检查JSON格式是否正确");
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public GenericResult<?> handleNullPointerException(NullPointerException e) {
        log.debug("全局捕获空指针异常: {}", ThrowableHelper.getStackTraceAsString(e));
        return GenericResult.of(500, "系统异常：空指针异常");
    }

}