package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.RecordDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
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
    @Transient
    @Schema(description = "数量")
    private transient Long count;
}
