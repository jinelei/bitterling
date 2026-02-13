package com.jinelei.bitterling.domain.request;


import com.jinelei.bitterling.domain.enums.BookmarkType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BookmarkListRequest", description = "书签列表请求")
public record BookmarkListRequest(
        @Schema(name = "id", description = "书签ID")
        Long id,
        @Schema(name = "name", description = "书签名称")
        String name,
        @Schema(name = "type", description = "书签类型")
        BookmarkType type,
        @Schema(name = "url", description = "书签地址")
        String url
) {
}
