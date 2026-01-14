package com.jinelei.bitterling.web.domain.pk;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 备忘录&标签主键
 *
 * @author jinelei
 * @version 1.0.0
 * @date 2026-01-11
 */
@Data
@ToString
@EqualsAndHashCode
public class MemoTagPrimaryKey {
    @Column(name = "memo_id")
    @Schema(description = "备忘id")
    private Long memoId;
    @Column(name = "tag_id")
    @Schema(description = "备忘标签id")
    private Long tagId;
}
