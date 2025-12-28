package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Schema(title = "书签领域对象", description = "书签领域对象")
public class BookmarkDomain extends BaseDomain<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = {Views.Query.class, Views.Delete.class, Views.Update.class})
    @Schema(description = "主键ID")
    private Long id;
    @JsonView(value = {Views.Query.class, Views.Delete.class, Views.Update.class})
    @Schema(description = "父级ID")
    private Long parentId;
    @Column(name = "name")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签名称")
    private String name;
    @Column(name = "type")
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签类型")
    private BookmarkType type;
    @Column(name = "url", unique = true)
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签地址")
    private String url;
    @Column(name = "icon", length = 10240)
    @JsonView(value = {Views.Query.class, Views.Create.class, Views.Update.class})
    @Schema(description = "书签图标")
    private String icon;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BookmarkDomain that = (BookmarkDomain) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(name, that.name) && type == that.type && Objects.equals(url, that.url) && Objects.equals(icon, that.icon) && Objects.equals(orderNumber, that.orderNumber) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, name, type, url, icon, orderNumber, createTime, updateTime);
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
                ", orderNumber=" + orderNumber +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
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
