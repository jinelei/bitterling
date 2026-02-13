package com.jinelei.bitterling.domain.request;

import com.jinelei.bitterling.domain.enums.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Schema(name = "BookmarkCreateRequest", description = "书签创建请求")
public record BookmarkCreateRequest(
        @Schema(name = "name", description = "书签名称")
        @NotBlank(message = "名称不能为空")
        String name,
        @NotNull(message = "类型不能为空")
        @Schema(name = "type", description = "书签类型")
        BookmarkType type,
        @Schema(name = "url", description = "书签地址")
        @NotBlank(message = "地址不能为空")
        String url,
        @Schema(name = "icon", description = "书签图标")
        @NotBlank(message = "图标不能为空")
        String icon,
        @Schema(name = "color", description = "书签颜色")
        @NotBlank(message = "颜色不能为空")
        String color,
        @Schema(name = "parentId", description = "父级ID")
        Long parentId
) {
}