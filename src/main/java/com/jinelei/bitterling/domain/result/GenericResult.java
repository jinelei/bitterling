package com.jinelei.bitterling.domain.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Schema(name = "GenericResult", description = "通用响应对象")
@SuppressWarnings("unused")
@JsonPropertyOrder({"code", "message", "data"})
public class GenericResult<T> {
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_FAILURE_INTERNAL = 500;
    public static final Integer CODE_FAILURE_UNAUTHORIZED = 401;
    public static final String MESSAGE_SUCCESS = "操作成功";
    public static final String MESSAGE_FAILURE_INTERNAL = "内部错误";
    public static final String MESSAGE_FAILURE_BUSINESS = "业务错误";
    public static final String LOGIN_FAILED = "Login failed";
    public static final String LOGIN_SUCCESSFUL = "Login successful";
    public static final String LOGOUT_SUCCESSFUL = "Logout successful";
    public static final String USER_NOT_LOGGED_IN = "User not logged in";
    public static final String PARAMETER_VALIDATION_FAILED = "Parameter validation failed";
    public static final String PARAMETER_BINDING_FAILED = "Parameter binding failed";
    public static final String FAILED_TO_PARSE_REQUEST_BODY = "Failed to parse request body";

    @Schema(name = "code", description = "错误代码")
    protected Integer code;
    @Schema(name = "message", description = "错误信息")
    protected String message;
    @Schema(name = "data", description = "响应数据")
    protected T data;

    public GenericResult() {
        this.code = CODE_SUCCESS;
        this.message = MESSAGE_SUCCESS;
    }

    public GenericResult(T data) {
        this.code = CODE_SUCCESS;
        this.message = MESSAGE_SUCCESS;
        this.data = data;
    }

    public GenericResult(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

}
