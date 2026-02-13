package com.jinelei.bitterling.domain.result;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jinelei.bitterling.constant.PageableProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(name = "PageableResult", description = "分页响应对象")
@SuppressWarnings("unused")
@JsonPropertyOrder({"code", "message", "pageNo", "pageSize", "total", "data"})
public class PageableResult<T extends Collection<?>> extends GenericResult<T> {
    public static final Long DEFAULT_TOTAL = 0L;
    @Schema(name = "pageNo", description = "分页页码")
    protected Integer pageNo;
    @Schema(name = "pageSize", description = "分页大小")
    protected Integer pageSize;
    @Schema(name = "total", description = "总计")
    protected Long total;

    public static <T extends Collection<?>> PageableResult<T> of(T data) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(PageableProperty.DEFAULT_PAGE_NO);
        result.setPageSize(PageableProperty.DEFAULT_PAGE_SIZE);
        result.setTotal(data != null ? data.size() : DEFAULT_TOTAL);
        result.setData(data);
        return result;
    }

    public static <T extends Collection<?>> PageableResult<T> of(T data, Long total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(PageableProperty.DEFAULT_PAGE_NO);
        result.setPageSize(PageableProperty.DEFAULT_PAGE_SIZE);
        result.setTotal(total);
        result.setData(data);
        return result;
    }

    public static <T extends Collection<?>> PageableResult<T> of(Integer pageNo, Integer pageSize, T data) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(data != null ? data.size() : DEFAULT_TOTAL);
        result.setData(data);
        return result;
    }

    public static <T extends Collection<?>> PageableResult<T> of(Integer pageNo, Integer pageSize, T data, Long total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setData(data);
        return result;
    }

    public static <T extends Collection<?>> PageableResult<T> of(Integer code, String message, Integer pageNo,
                                                                 Integer pageSize, T data, Long total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setData(data);
        return result;
    }


}
