package com.jinelei.bitterling.core.exception;

/**
 * 自定义业务异常
 */
public class BusinessException extends RuntimeException {
    private Integer code;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(String message) {
        this(500, message);
    }

    public Integer getCode() {
        return code;
    }
}
