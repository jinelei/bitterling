package com.jinelei.bitterling.core.constant;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "默认分页配置")
public class PageablePropertity {
    @Schema(name = "默认分页页码")
    public static Integer DEFAULT_PAGE_NO = 1;
    @Schema(name = "默认分页大小")
    public static Integer DEFAULT_PAGE_SIZE = 10;

    public void setDefaultPageNo(Integer defaultPageNo) {
        DEFAULT_PAGE_NO = defaultPageNo;
    }

    public void setDefaultPageSize(Integer defaultPageSize) {
        DEFAULT_PAGE_SIZE = defaultPageSize;
    }

}
