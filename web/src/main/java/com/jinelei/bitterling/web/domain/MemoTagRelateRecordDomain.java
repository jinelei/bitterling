package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.EmbeddedRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
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
public class MemoTagRelateRecordDomain extends EmbeddedRecordDomain<MemoTagPrimaryKey> implements Serializable {

}
