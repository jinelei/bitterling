package com.jinelei.bitterling.domain.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Schema(name = "MemoCreateRequest", description = "备忘创建请求")
public record MemoCreateRequest(
        @Schema(name = "title", description = "备忘标题")
        @NotBlank(message = "标题不能为空")
        String title,
        @Schema(name = "subTitle", description = "副备忘标题")
        @NotBlank(message = "副标题不能为空")
        String subTitle,
        @Schema(name = "content", description = "备忘内容")
        @NotBlank(message = "内容不能为空")
        String content,
        @Schema(name = "orderNumber", description = "书签排序")
        Integer orderNumber,
        @Schema(name = "tagIds", description = "备忘关联标签列表")
        List<Long> tagIds) {
}
