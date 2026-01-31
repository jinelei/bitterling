package com.jinelei.bitterling.domain;

import com.jinelei.bitterling.domain.base.RecordDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MEMO_TAG_RELATE")
@Schema(name = "MemoTagRelateRecordDomain", description = "备忘标签关联领域对象")
public class MemoTagRelateRecordDomain extends RecordDomain<Long> implements Serializable {
    @Column(name = "memo_id", nullable = false)
    @Schema(name = "memoId", description = "备忘id")
    private Long memoId;
    @Column(name = "tag_id", nullable = false)
    @Schema(name = "tagId", description = "备忘标签id")
    private Long tagId;
}
