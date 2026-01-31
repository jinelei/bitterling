package com.jinelei.bitterling.domain.request;

import com.jinelei.bitterling.constant.PageableProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode
@Schema(name = "PageableRequest", description = "分页请求对象")
@SuppressWarnings({"unused"})
public class PageableRequest<T> {
    @Schema(name = "pageNo", description = "分页页码")
    protected Integer pageNo = PageableProperty.DEFAULT_PAGE_NO;
    @Schema(name = "pageSize", description = "分页大小")
    protected Integer pageSize = PageableProperty.DEFAULT_PAGE_SIZE;
    @Schema(name = "query", description = "查询条件")
    protected T query;
}
