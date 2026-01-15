package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.RecordDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
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
        @Lob
        @Column(name = "content", columnDefinition = "MEDIUMTEXT")
        @Schema(description = "备忘内容")
        private String content;

        public record CreateRequest(
                        @NotBlank(message = "标题不能为空") String title,
                        @NotBlank(message = "副标题不能为空") String subTitle,
                        @NotBlank(message = "内容不能为空") String content,
                        Integer orderNumber,
                        List<Long> tagIds) {
        }

        public record DeleteRequest(
                        @NotNull(message = "ID不能为空") Long id) {
        }

        public record UpdateRequest(
                        @NotNull(message = "ID不能为空") Long id,
                        @NotBlank(message = "标题不能为空") String title,
                        @NotBlank(message = "副标题不能为空") String subTitle,
                        @NotBlank(message = "内容不能为空") String content,
                        @NotNull(message = "排序不能为空") Integer orderNumber,
                        List<Long> tagIds) {
        }

        public record ListQueryRequest(
                        Long id,
                        String title,
                        String subTitle,
                        String content,
                        Integer orderNumber) {
        }

        public record DetailResponse(
                        Long id,
                        String title,
                        String subTitle,
                        String content,
                        String contentRender,
                        List<MemoTagDomain> tags,
                        List<Long> tagIds,
                        Integer orderNumber,
                        LocalDateTime createTime,
                        LocalDateTime updateTime) {
        }

}
