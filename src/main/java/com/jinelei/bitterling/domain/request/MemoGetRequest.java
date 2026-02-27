package com.jinelei.bitterling.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "MemoGetRequest", description = "备忘查询ID请求")
public record MemoGetRequest(
        @Schema(name = "id", description = "备忘ID")
        @NotNull(message = "id不能为空")
        Long id) {
}