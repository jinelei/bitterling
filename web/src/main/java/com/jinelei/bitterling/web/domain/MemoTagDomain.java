package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.RecordDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MEMO_TAG")
@Schema(title = "备忘标签领域对象", description = "备忘标签领域对象")
public class MemoTagDomain extends RecordDomain<Long> {
    @Column(name = "title")
    @Schema(description = "备忘标签标题")
    private String title;
    @Column(name = "icon")
    @Schema(description = "备忘标签图标")
    private String icon;

    @Schema(title = "普通备忘对象")
    public record Response(
            @NotNull(message = "备忘标签id不能为空") Long id,
            @NotNull(message = "备忘标签标题不能为空") String title,
            @NotNull(message = "备忘标签图标不能为空") String icon) {
    }

    @Schema(title = "带总数备忘对象")
    public record CountResponse(
            @NotNull(message = "备忘标签id不能为空") Long id,
            @NotNull(message = "备忘标签标题不能为空") String title,
            @NotNull(message = "备忘标签图标不能为空") String icon,
            @NotNull(message = "备忘标签数量不能为空") Long count) {
    }
}
