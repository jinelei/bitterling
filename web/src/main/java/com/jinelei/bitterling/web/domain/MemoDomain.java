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
@Table(name = "MEMO")
@Schema(title = "备忘领域对象", description = "备忘领域对象")
public class MemoDomain extends BaseDomain<Long> implements Comparable<MemoDomain> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {BaseView.Query.class, BaseView.Delete.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotNull(message = "ID不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "主键ID")
    private Long id;
    @Column(name = "title")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘标题不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘标题")
    private String title;
    @Column(name = "sub_title")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘副标题不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘副标题")
    private String subTitle;
    @Column(name = "content")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘内容")
    private String content;
    @Transient
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class, View.Render.class})
    @NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘内容")
    private transient String contentRender;
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
    public int compareTo(MemoDomain o) {
        return Optional.ofNullable(o.getCreateTime())
                .map(s -> s.compareTo(
                        Optional.ofNullable(o).map(MemoDomain::getCreateTime).orElse(LocalDateTime.now())))
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

    public @NotBlank(message = "备忘标题不能为空", groups = {BaseView.Persist.class}) String getTitle() {
        return title;
    }

    public void setTitle(@NotBlank(message = "备忘标题不能为空", groups = {BaseView.Persist.class}) String title) {
        this.title = title;
    }

    public @NotBlank(message = "备忘副标题不能为空", groups = {BaseView.Persist.class}) String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(@NotBlank(message = "备忘副标题不能为空", groups = {BaseView.Persist.class}) String subTitle) {
        this.subTitle = subTitle;
    }

    public @NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class}) String getContent() {
        return content;
    }

    public void setContent(@NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class}) String content) {
        this.content = content;
    }

    public @NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class}) String getContentRender() {
        return contentRender;
    }

    public void setContentRender(@NotBlank(message = "备忘内容不能为空", groups = {BaseView.Persist.class}) String contentRender) {
        this.contentRender = contentRender;
    }

    public @NotNull(message = "创建时间不能为空", groups = {BaseView.Persist.class}) LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(@NotNull(message = "创建时间不能为空", groups = {BaseView.Persist.class}) LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public @NotNull(message = "更新时间不能为空", groups = {BaseView.Persist.class}) LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(@NotNull(message = "更新时间不能为空", groups = {BaseView.Persist.class}) LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoDomain that = (MemoDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(subTitle, that.subTitle) && Objects.equals(content, that.content) && Objects.equals(contentRender, that.contentRender) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, subTitle, content, contentRender, createTime, updateTime);
    }

    @Override
    public String toString() {
        return "MemoDomain{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", content='" + content + '\'' +
                ", contentRender='" + contentRender + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                "} " + super.toString();
    }

    public static interface View extends BaseView {
        public static interface Render {
        }
    }
}
