package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;

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
    @JsonView(value = { Views.Query.class, Views.Delete.class, Views.Update.class })
    @Schema(description = "主键ID")
    private Long id;
    @Column(name = "name")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "书签名称")
    private String name;
    @Column(name = "type")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "书签类型")
    private BookmarkType type;
    @Column(name = "url")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "书签地址")
    private String url;
    @Column(name = "icon", length = 10240)
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "书签图标")
    private String icon;
    @Column(name = "order_number")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "排序值")
    private Integer orderNumber;
    @Column(name = "create_time")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        result = prime * result + ((icon == null) ? 0 : icon.hashCode());
        result = prime * result + ((orderNumber == null) ? 0 : orderNumber.hashCode());
        result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
        result = prime * result + ((updateTime == null) ? 0 : updateTime.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BookmarkDomain other = (BookmarkDomain) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (type != other.type)
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        if (icon == null) {
            if (other.icon != null)
                return false;
        } else if (!icon.equals(other.icon))
            return false;
        if (orderNumber == null) {
            if (other.orderNumber != null)
                return false;
        } else if (!orderNumber.equals(other.orderNumber))
            return false;
        if (createTime == null) {
            if (other.createTime != null)
                return false;
        } else if (!createTime.equals(other.createTime))
            return false;
        if (updateTime == null) {
            if (other.updateTime != null)
                return false;
        } else if (!updateTime.equals(other.updateTime))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BookmarkDomain [id=" + id + ", name=" + name + ", type=" + type + ", url=" + url + ", icon=" + icon
                + ", orderNumber=" + orderNumber + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
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
