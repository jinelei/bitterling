package com.jinelei.bitterling.web.domain.pk;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.view.BaseView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

/**
 * 备忘录&标签主键
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2026-01-11
 */
public class MemoTagPrimaryKey {
    @Column(name = "memo_id")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘id不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘id")
    private Long memoId;
    @Column(name = "tag_id")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class, BaseView.Detail.class, BaseView.List.class})
    @NotBlank(message = "备忘标签id不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "备忘标签id")
    private Long tagId;

    public Long getMemoId() {
        return memoId;
    }

    public void setMemoId(Long memoId) {
        this.memoId = memoId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MemoTagPrimaryKey that = (MemoTagPrimaryKey) o;
        return Objects.equals(memoId, that.memoId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(memoId, tagId);
    }

    @Override
    public String toString() {
        return "MemoTagPrimaryKey{" +
                "memoId=" + memoId +
                ", tagId=" + tagId +
                '}';
    }
}
