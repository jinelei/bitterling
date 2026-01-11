package com.jinelei.bitterling.web.domain.dto;

import java.util.Objects;

/**
 * 页面请求对象
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2026-01-11
 */
public class MemoPageRequest {
    private Long id;
    private Long tagId;
    private String title;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoPageRequest that = (MemoPageRequest) o;
        return Objects.equals(id, that.id) && Objects.equals(tagId, that.tagId) && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tagId, title);
    }

    @Override
    public String toString() {
        return "MemoPageRequest{" +
                "id=" + id +
                ", tagId='" + tagId + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
