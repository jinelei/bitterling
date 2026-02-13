package com.jinelei.bitterling.domain.request;


import com.jinelei.bitterling.domain.MemoTagDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "MemoResponse", description = "备忘详情响应")
public record MemoResponse(
        @Schema(name = "title", description = "备忘标题")
        @NotNull(message = "ID不能为空") Long id,
        String title,
        @Schema(name = "subTitle", description = "副备忘标题")
        String subTitle,
        @Schema(name = "content", description = "备忘内容")
        String content,
        @Schema(name = "tags", description = "备忘关联标签对象列表")
        List<MemoTagDomain> tags,
        @Schema(name = "tagIds", description = "备忘关联标签列表")
        List<Long> tagIds,
        @Schema(name = "orderNumber", description = "书签排序")
        Integer orderNumber,
        @Schema(name = "createTime", description = "创建时间")
        LocalDateTime createTime,
        @Schema(name = "updateTime", description = "更新时间")
        LocalDateTime updateTime) {
}