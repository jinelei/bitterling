package com.jinelei.bitterling.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "MemoDeleteRequest", description = "备忘删除请求")
public record MemoDeleteRequest(
        @Schema(name = "id", description = "备忘ID")
        @NotNull(message = "ID不能为空") Long id) {
}
