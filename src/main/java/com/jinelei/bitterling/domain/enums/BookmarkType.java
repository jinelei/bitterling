package com.jinelei.bitterling.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "BookmarkType", description = "书签类型")
public enum BookmarkType {
    FOLDER,
    ITEM,
    ;
}
