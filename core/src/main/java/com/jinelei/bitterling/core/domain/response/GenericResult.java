package com.jinelei.bitterling.core.domain.response;

import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "通用响应对象")
@SuppressWarnings("rawtypes")
@JsonPropertyOrder({"code", "message", "data"})
public class GenericResult<T> {
    public static final Integer CODE_SUCCESS = 200;
    public static final Integer CODE_FAILURE_INTERNAL = 500;
    public static final String MESSAGE_SUCCESS = "操作成功";
    public static final String MESSAGE_FAILURE_INTERNAL = "内部错误";

    @Schema(name = "错误代码")
    protected Integer code;
    @Schema(name = "错误信息")
    protected String message;
    @Schema(name = "响应数据")
    protected T data;

    public static <T> GenericResult<T> of(T data) {
        GenericResult<T> result = new GenericResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setData(data);
        return result;
    }

    public static GenericResult<Void> of(Integer code, String message) {
        GenericResult<Void> result = new GenericResult<>();
        result.setCode(code);
        result.setMessage(message);
        return result;
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

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((data == null) ? 0 : data.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GenericResult other = (GenericResult) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (data == null) {
            if (other.data != null)
                return false;
        } else if (!data.equals(other.data))
            return false;
        return true;
    }

}
