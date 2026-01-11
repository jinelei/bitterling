package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.RecordDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MEMO")
@Schema(title = "备忘领域对象", description = "备忘领域对象")
public class MemoDomain extends RecordDomain<Long> {
    @Column(name = "title")
    @Schema(description = "备忘标题")
    private String title;
    @Column(name = "sub_title")
    @Schema(description = "备忘副标题")
    private String subTitle;
    @Column(name = "content")
    @Schema(description = "备忘内容")
    private String content;
    @Transient
    @Schema(description = "备忘内容")
    private transient String contentRender;
    @Transient
    @Schema(description = "备忘标签")
    private transient List<MemoTagDomain> tags;

    public record CreateRequest(
            @NotNull(message = "标题不能为空") String title,
            @NotBlank(message = "副标题不能为空") String subTitle,
            @NotBlank(message = "内容不能为空") String content,
            @NotBlank(message = "排序不能为空") Integer orderNumber

    ) {
    }

    public record UpdateRequest(
            @NotNull(message = "ID不能为空") Long id,
            @NotNull(message = "标题不能为空") String title,
            @NotBlank(message = "副标题不能为空") String subTitle,
            @NotBlank(message = "内容不能为空") String content,
            @NotBlank(message = "排序不能为空") Integer orderNumber) {
    }

    public record DeleteRequest(
            @NotNull(message = "ID不能为空") Long id) {
    }

}
