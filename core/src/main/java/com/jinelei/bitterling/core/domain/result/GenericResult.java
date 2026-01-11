package com.jinelei.bitterling.core.domain.result;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Schema(name = "通用响应对象")
@SuppressWarnings("unused")
@JsonPropertyOrder({"code", "message", "data"})
public class GenericResult<T> {
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_FAILURE_INTERNAL = 500;
    public static final String MESSAGE_SUCCESS = "操作成功";
    public static final String MESSAGE_FAILURE_INTERNAL = "内部错误";
    public static final String MESSAGE_FAILURE_BUSINESS = "业务错误";

    @Schema(name = "错误代码")
    protected Integer code;
    @Schema(name = "错误信息")
    protected String message;
    @Schema(name = "响应数据")
    protected T data;

    public static <T> GenericResult<T> success(T data) {
        return of(CODE_SUCCESS, MESSAGE_SUCCESS, data);
    }

    public static <T> GenericResult<T> failure(T data) {
        return of(CODE_FAILURE_INTERNAL, MESSAGE_FAILURE_INTERNAL, data);
    }

    public static <T> GenericResult<T> failure(String message, T data) {
        return of(CODE_FAILURE_INTERNAL, message, data);
    }

    public static <T> GenericResult<T> failure(Integer code, T data) {
        return of(code, MESSAGE_FAILURE_INTERNAL, data);
    }

    public static <T> GenericResult<T> failure(Integer code, String message, T data) {
        return of(code, message, data);
    }

    public static <T> GenericResult<T> of(Integer code, String message, T data) {
        GenericResult<T> result = new GenericResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setData(data);
        return result;
    }

    public static <T> boolean isSuccess(GenericResult<T> result) {
        return Optional.ofNullable(result).map(GenericResult::getCode).map(CODE_SUCCESS::equals).orElse(false);
    }


}
