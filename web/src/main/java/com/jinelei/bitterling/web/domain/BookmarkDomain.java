package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.domain.view.TreeView;
import com.jinelei.bitterling.web.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "BOOKMARK")
@Schema(title = "书签领域对象", description = "书签领域对象")
public class BookmarkDomain extends BaseDomain<Long>
        implements Comparable<BookmarkDomain>, TreeView<BookmarkDomain, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.Query.class, Views.Delete.class, Views.Update.class})
    @Schema(description = "主键ID")
    private Long id;
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Delete.class, Views.Update.class})
    @Schema(description = "父级ID")
    private Long parentId;
    @Column(name = "name", unique = true)
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签名称")
    private String name;
    @Column(name = "type")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签类型")
    private BookmarkType type;
    @Column(name = "url")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签地址")
    private String url;
    @Column(name = "icon")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签图标")
    private String icon;
    @Column(name = "color")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签颜色")
    private String color;
    @Column(name = "order_number")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "排序值")
    private Integer orderNumber;
    @Column(name = "create_time")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
    @Transient
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "子级")
    private transient List<BookmarkDomain> children;

    @Override
    public int compareTo(BookmarkDomain o) {
        return Optional.ofNullable(o.getName())
                .map(s -> s.compareTo(Optional.ofNullable(o).map(BookmarkDomain::getName).orElse("")))
                .orElse(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
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

    public List<BookmarkDomain> getChildren() {
        return children;
    }

    public void setChildren(List<BookmarkDomain> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkDomain that = (BookmarkDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(name, that.name) && type == that.type && Objects.equals(url, that.url) && Objects.equals(icon, that.icon) && Objects.equals(color, that.color) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(children, that.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, type, url, icon, color, orderNumber, createTime, updateTime, children);
    }

    @Override
    public String toString() {
        return "BookmarkDomain{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", url='" + url + '\'' +
                ", icon='" + icon + '\'' +
                ", color='" + color + '\'' +
                ", orderNumber=" + orderNumber +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", children=" + children +
                "} " + super.toString();
    }

    public static interface Views {
        public static interface Create {
        }

        public static interface Delete {
        }

        public static interface Update {
        }

        public static interface Query {
        }
    }
}
