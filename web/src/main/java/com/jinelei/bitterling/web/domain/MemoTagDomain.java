package com.jinelei.bitterling.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

@Entity
@Table(name = "MEMO_TAG")
@Schema(title = "备忘标签领域对象", description = "备忘标签领域对象")
public class MemoTagDomain extends RecordDomain<Long> {
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
    @Transient
    @JsonView(value = {BaseView.Detail.class, BaseView.List.class})
    @Schema(description = "数量")
    private transient Long count;

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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MemoTagDomain that = (MemoTagDomain) o;
        return Objects.equals(title, that.title) && Objects.equals(icon, that.icon) && Objects.equals(count, that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), title, icon, count);
    }

    @Override
    public String toString() {
        return "MemoTagDomain{" +
                "title='" + title + '\'' +
                ", icon='" + icon + '\'' +
                ", count=" + count +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

}
