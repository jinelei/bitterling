package com.jinelei.bitterling.web.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;

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
    @Column(name = "url")
    @JsonView(value = { Views.Query.class, Views.Create.class, Views.Update.class })
    @Schema(description = "书签地址")
    private String url;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
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
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "BookmarkDomain [id=" + id + ", name=" + name + ", url=" + url + "]";
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
