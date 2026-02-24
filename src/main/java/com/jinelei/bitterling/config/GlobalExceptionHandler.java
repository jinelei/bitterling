package com.jinelei.bitterling.config;

import com.jinelei.bitterling.domain.result.ResultFactory;
import com.jinelei.bitterling.domain.result.StringResult;
import com.jinelei.bitterling.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.utils.ThrowableUtils;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 处理自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public GenericResult<String> handleBusinessException(BusinessException e) {
        log.debug("全局捕获业务异常: {}", ThrowableUtils.getStackTraceAsString(e));
        return ResultFactory.create(StringResult.class, e.getCode(), GenericResult.MESSAGE_FAILURE_BUSINESS, e.getMessage());
    }

    /**
     * 处理请求参数校验异常（@RequestBody 注解的参数）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public GenericResult<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.debug("全局捕获校验异常: {}", ThrowableUtils.getStackTraceAsString(e));
        String errorMsg = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_INTERNAL, GenericResult.PARAMETER_VALIDATION_FAILED, errorMsg);
    }

    /**
     * 处理请求参数绑定异常（如参数类型不匹配）
     */
    @ExceptionHandler(BindException.class)
    public GenericResult<String> handleBindException(BindException e) {
        log.debug("全局捕获绑定异常: {}", ThrowableUtils.getStackTraceAsString(e));
        String errorMsg = e.getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("；"));
        return ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_INTERNAL, GenericResult.PARAMETER_BINDING_FAILED, errorMsg);
    }

    /**
     * 处理请求体解析异常（如JSON格式错误）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public GenericResult<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.debug("全局捕获解析异常: {}", ThrowableUtils.getStackTraceAsString(e));
        return ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_INTERNAL, GenericResult.FAILED_TO_PARSE_REQUEST_BODY, "请检查JSON格式是否正确");
    }

    /**
     * 处理空指针异常
     */
    @ExceptionHandler(NullPointerException.class)
    public GenericResult<String> handleNullPointerException(NullPointerException e) {
        log.debug("全局捕获空指针异常: {}", ThrowableUtils.getStackTraceAsString(e));
        return ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_INTERNAL, GenericResult.MESSAGE_FAILURE_BUSINESS, "空指针异常");
    }

    /**
     * 处理所有未捕获的未知异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    public GenericResult<String> handleGlobalException(Exception e) {
        log.error("全局捕获未知异常: {}", ThrowableUtils.getStackTraceAsString(e));
        return ResultFactory.create(StringResult.class, GenericResult.CODE_FAILURE_INTERNAL, GenericResult.MESSAGE_FAILURE_BUSINESS, e.getMessage());
    }

}
