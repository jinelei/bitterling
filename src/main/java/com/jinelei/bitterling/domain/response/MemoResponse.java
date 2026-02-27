package com.jinelei.bitterling.domain.response;

import com.jinelei.bitterling.domain.MemoTagDomain;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

@Schema(name = "MemoResponse", description = "备忘响应对象")
public record MemoResponse(
        @Schema(name = "id", description = "主键ID")
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
        String content,
        @Schema(name = "tags", description = "备忘关联标签对象列表")
        List<MemoTagDomain> tags,
        @Schema(name = "tagIds", description = "备忘关联标签列表")
        List<Long> tagIds
) {
}
