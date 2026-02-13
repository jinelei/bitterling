package com.jinelei.bitterling.domain;

import com.jinelei.bitterling.domain.base.RecordDomain;
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
@Schema(name = "MemoTagDomain", description = "备忘标签领域对象")
public class MemoTagDomain extends RecordDomain<Long> {
    @Column(name = "title")
    @Schema(name = "title", description = "备忘标签标题")
    private String title;
    @Column(name = "icon")
    @Schema(name = "icon", description = "备忘标签图标")
    private String icon;



}
