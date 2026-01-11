package com.jinelei.bitterling.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "MEMO_TAG_RELATE")
@Schema(title = "备忘标签关联领域对象", description = "备忘标签关联领域对象")
public class MemoTagRelateDomain extends BaseDomain<MemoTagPrimaryKey> implements Comparable<MemoTagRelateDomain> {
    @EmbeddedId
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {BaseView.Query.class, BaseView.Delete.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotNull(message = "ID不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "主键ID")
    private MemoTagPrimaryKey id;
    @Column(name = "create_time")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotNull(message = "创建时间不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotNull(message = "更新时间不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Override
    public int compareTo(MemoTagRelateDomain o) {
        return Optional.ofNullable(o.getCreateTime())
                .map(s -> s.compareTo(
                        Optional.ofNullable(o).map(MemoTagRelateDomain::getCreateTime).orElse(LocalDateTime.now())))
                .orElse(0);
    }

    @Override
    public MemoTagPrimaryKey getId() {
        return id;
    }

    @Override
    public void setId(MemoTagPrimaryKey id) {
        this.id = id;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoTagRelateDomain that = (MemoTagRelateDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "MemoTagRelateDomain{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
