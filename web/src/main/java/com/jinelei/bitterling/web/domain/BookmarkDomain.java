package com.jinelei.bitterling.web.domain;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.TreeRecordDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import com.jinelei.bitterling.core.domain.view.TreeView;
import com.jinelei.bitterling.web.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "BOOKMARK")
@Schema(title = "书签领域对象", description = "书签领域对象")
public class BookmarkDomain extends TreeRecordDomain<Long> implements TreeView<BookmarkDomain, Long> {
    @Column(name = "name", unique = true)
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotBlank(message = "书签名称不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "书签名称")
    private String name;
    @Column(name = "type")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotNull(message = "书签类型不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "书签类型")
    private BookmarkType type;
    @Column(name = "url")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @Schema(description = "书签地址")
    private String url;
    @Column(name = "icon")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @Schema(description = "书签图标")
    private String icon;
    @Column(name = "color")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @Schema(description = "书签颜色")
    private String color;
    @Transient
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @Schema(description = "子级")
    protected transient List<BookmarkDomain> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookmarkType getType() {
        return type;
    }

    public void setType(BookmarkType type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public List<BookmarkDomain> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<BookmarkDomain> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BookmarkDomain that = (BookmarkDomain) o;
        return Objects.equals(name, that.name) && type == that.type && Objects.equals(url, that.url) && Objects.equals(icon, that.icon) && Objects.equals(color, that.color) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, type, url, icon, color, children);
    }

    @Override
    public String toString() {
        return "BookmarkDomain{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                ", children=" + children +
                ", id=" + id +
                ", parentId=" + parentId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

}
