package com.jinelei.bitterling.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "MEMO")
@Schema(title = "备忘领域对象", description = "备忘领域对象")
public class MemoDomain extends RecordDomain<Long> {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemoDomain that = (MemoDomain) o;
        return Objects.equals(title, that.title) && Objects.equals(subTitle, that.subTitle) && Objects.equals(content, that.content) && Objects.equals(contentRender, that.contentRender);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, subTitle, content, contentRender);
    }

    @Override
    public String toString() {
        return "MemoDomain{" +
                "title='" + title + '\'' +
                ", subTitle='" + subTitle + '\'' +
                ", content='" + content + '\'' +
                ", contentRender='" + contentRender + '\'' +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

    public static interface View extends BaseView {
        public static interface Render {
        }
    }
}
