package com.jinelei.bitterling.domain.result;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jinelei.bitterling.constant.PageableProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static com.jinelei.bitterling.constant.PageableProperty.DEFAULT_PAGE_NO;
import static com.jinelei.bitterling.constant.PageableProperty.DEFAULT_PAGE_SIZE;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(name = "PageableResult", description = "分页响应对象")
@SuppressWarnings("unused")
@JsonPropertyOrder({"code", "message", "pageNo", "pageSize", "total", "data"})
public class PageableResult<T extends Collection<?>> extends CollectionResult<T> {
    public static final Long DEFAULT_TOTAL = 0L;
    @Schema(name = "pageNo", description = "分页页码")
    protected Integer pageNo;
    @Schema(name = "pageSize", description = "分页大小")
    protected Integer pageSize;

    public PageableResult() {
        super();
        this.pageNo = DEFAULT_PAGE_NO;
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.total = DEFAULT_TOTAL;
    }

    public PageableResult(Integer code, String message, T data, Integer pageNo, Integer pageSize, Long total) {
        super(code, message, data, total);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public static <T extends Collection<?>> PageableResult<T> of(T data) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(DEFAULT_PAGE_NO);
        result.setPageSize(DEFAULT_PAGE_SIZE);
        result.setTotal(data != null ? data.size() : DEFAULT_TOTAL);
        result.setData(data);
        return result;
    }

    public static <T extends Collection<?>> PageableResult<T> of(T data, Long total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(DEFAULT_PAGE_NO);
        result.setPageSize(DEFAULT_PAGE_SIZE);
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
