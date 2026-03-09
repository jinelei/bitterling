package com.jinelei.bitterling.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(name = "BookmarkType", description = "书签类型")
public enum BookmarkType {
    FOLDER("文件夹"),
    BOOKMARK("书签"),
    ;
    private final String name;
}
