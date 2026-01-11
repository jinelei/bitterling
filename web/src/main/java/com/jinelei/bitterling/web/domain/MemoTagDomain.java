package com.jinelei.bitterling.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Entity
@Table(name = "MEMO_TAG")
@Schema(title = "备忘标签领域对象", description = "备忘标签领域对象")
public class MemoTagDomain extends BaseDomain<Long> implements Comparable<MemoTagDomain> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {BaseView.Query.class, BaseView.Delete.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotNull(message = "ID不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "主键ID")
    private Long id;
    @Column(name = "title")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘标签标题不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘标签标题")
    private String title;
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
    public int compareTo(MemoTagDomain o) {
        return Optional.ofNullable(o.getCreateTime())
                .map(s -> s.compareTo(
                        Optional.ofNullable(o).map(MemoTagDomain::getCreateTime).orElse(LocalDateTime.now())))
                .orElse(0);
    }

    @Override
    public @NotNull(message = "ID不能为空", groups = {BaseView.Persist.class}) Long getId() {
        return id;
    }

    @Override
    public void setId(@NotNull(message = "ID不能为空", groups = {BaseView.Persist.class}) Long id) {
        this.id = id;
    }

    public @NotBlank(message = "备忘标签标题不能为空", groups = {BaseView.Persist.class}) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "备忘标签标题不能为空", groups = {BaseView.Persist.class}) String title) {
        this.title = title;
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
        MemoTagDomain that = (MemoTagDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "MemoTagDomain{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }

}
