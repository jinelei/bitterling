package com.jinelei.bitterling.domain.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(name = "MemoDomain", description = "备忘领域对象")
public record MemoResponse(
        @Schema(name = "ID", description = "主键ID")
        Long id,
        @Schema(name = "createTime", description = "创建时间")
        LocalDateTime createTime,
        @Schema(name = "updateTime", description = "更新时间")
        LocalDateTime updateTime,
        @Schema(name = "orderNumber", description = "排序值")
        Integer orderNumber,
        @Schema(name = "title", description = "备忘标题")
        String title,
        @Schema(name = "subTitle", description = "备忘副标题")
        String subTitle,
        @Schema(name = "content", description = "备忘内容")
        String content
) {
}
