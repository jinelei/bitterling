package com.jinelei.bitterling.web.domain;

import com.jinelei.bitterling.core.domain.EmbeddedRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "MEMO_TAG_RELATE")
@Schema(title = "备忘标签关联领域对象", description = "备忘标签关联领域对象")
public class MemoTagRelateRecordDomain extends EmbeddedRecordDomain<MemoTagPrimaryKey> implements Serializable {

    @Override
    public String toString() {
        return "MemoTagRelateRecordDomain{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

}
