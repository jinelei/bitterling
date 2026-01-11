package com.jinelei.bitterling.core.domain.request;

import com.jinelei.bitterling.core.constant.PageableProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Schema(name = "分页请求对象")
@SuppressWarnings({"unused"})
public class PageableRequest<T> {
    @Schema(name = "分页页码")
    protected Integer pageNo = PageableProperty.DEFAULT_PAGE_NO;
    @Schema(name = "分页大小")
    protected Integer pageSize = PageableProperty.DEFAULT_PAGE_SIZE;
    @Schema(name = "查询条件")
    protected T query;
}