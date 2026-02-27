package com.jinelei.bitterling.domain;

import com.jinelei.bitterling.domain.base.RecordDomain;
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
@Schema(name = "MemoDomain", description = "备忘领域对象")
public class MemoDomain extends RecordDomain<Long> {
    @Column(name = "title")
    @Schema(name = "title", description = "备忘标题")
    private String title;
    @Column(name = "sub_title")
    @Schema(name = "subTitle", description = "备忘副标题")
    private String subTitle;
    @Lob
    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    @Schema(name = "content", description = "备忘内容")
    private String content;
    @Transient
    @Schema(name = "tags", description = "备忘关联标签")
    private transient List<MemoTagDomain> tags;
}
