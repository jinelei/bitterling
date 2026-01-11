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
    @Column(name = "icon")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘标签图标不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘标签图标")
    private String icon;
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

    @Transient
    @JsonView(value = {BaseView.Detail.class, BaseView.List.class})
    @Schema(description = "数量")
    private transient Long count;

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

    public @NotBlank(message = "备忘标签图标不能为空", groups = {BaseView.Persist.class}) String getIcon() {
        return icon;
    }

    public void setIcon(@NotBlank(message = "备忘标签图标不能为空", groups = {BaseView.Persist.class}) String icon) {
        this.icon = icon;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoTagDomain that = (MemoTagDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(icon, that.icon) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, icon, createTime, updateTime, count);
    }

    @Override
    public String toString() {
        return "MemoTagDomain{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", count=" + count +
                "} " + super.toString();
    }

}
