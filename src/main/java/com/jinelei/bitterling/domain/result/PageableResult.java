package com.jinelei.bitterling.domain.result;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

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

    public PageableResult(T data, Long total, Integer pageNo, Integer pageSize) {
        super(data, total);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

    public PageableResult(Integer code, String message, T data, Long total, Integer pageNo, Integer pageSize) {
        super(code, message, data, total);
        this.pageNo = pageNo;
        this.pageSize = pageSize;
    }

}
