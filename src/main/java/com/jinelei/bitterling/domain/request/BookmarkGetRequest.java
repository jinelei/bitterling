package com.jinelei.bitterling.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "BookmarkGetRequest", description = "书签查询ID请求")
public record BookmarkGetRequest(
        @Schema(name = "id", description = "书签ID")
        @NotNull(message = "id不能为空")
        Long id) {
}