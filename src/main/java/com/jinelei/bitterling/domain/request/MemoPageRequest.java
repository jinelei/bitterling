package com.jinelei.bitterling.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "MemoPageRequest", description = "备忘分页请求")
public record MemoPageRequest(
        @Schema(name = "tagId", description = "备忘关联标签")
        Long tagId
) {
}