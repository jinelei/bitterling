package com.jinelei.bitterling.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(name = "MemoTagResponse", description = "备忘标签对象")
public record MemoTagResponse(
        @Schema(name = "id", description = "备忘标签ID")
        @NotNull(message = "备忘标签id不能为空")
        Long id,
        @Schema(name = "title", description = "备忘标签标题")
        @NotNull(message = "备忘标签标题不能为空")
        String title,
        @Schema(name = "icon", description = "备忘标签图标")
        @NotNull(message = "备忘标签图标不能为空")
        String icon) {
}
