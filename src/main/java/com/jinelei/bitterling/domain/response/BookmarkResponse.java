package com.jinelei.bitterling.domain.response;

import com.jinelei.bitterling.domain.enums.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "BookmarkDomain", description = "书签领域对象")
public record BookmarkResponse(
        @Schema(name = "ID", description = "主键ID")
        Long id,
        @Schema(name = "createTime", description = "创建时间")
        LocalDateTime createTime,
        @Schema(name = "updateTime", description = "更新时间")
        LocalDateTime updateTime,
        @Schema(name = "orderNumber", description = "排序值")
        Integer orderNumber,
        @Schema(name = "name", description = "书签名称")
        String name,
        @Schema(name = "type", description = "书签类型")
        BookmarkType type,
        @Schema(name = "url", description = "书签地址")
        String url,
        @Schema(name = "icon", description = "书签图标")
        String icon,
        @Schema(name = "color", description = "书签颜色")
        String color,
        @Schema(name = "children", description = "子级")
        List<BookmarkResponse> children
) {
}
