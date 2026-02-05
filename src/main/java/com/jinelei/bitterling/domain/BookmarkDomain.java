package com.jinelei.bitterling.domain;

import java.util.List;

import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import com.jinelei.bitterling.domain.view.TreeView;
import com.jinelei.bitterling.domain.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BOOKMARK")
@Schema(name = "BookmarkDomain", description = "书签领域对象")
public class BookmarkDomain extends TreeRecordDomain<Long> implements TreeView<BookmarkDomain, Long> {
    @Column(name = "name", unique = true)
    @Schema(name = "name", description = "书签名称")
    private String name;
    @Column(name = "type")
    @Schema(name = "type", description = "书签类型")
    private BookmarkType type;
    @Column(name = "url")
    @Schema(name = "url", description = "书签地址")
    private String url;
    @Column(name = "icon")
    @Schema(name = "icon", description = "书签图标")
    private String icon;
    @Column(name = "color")
    @Schema(name = "color", description = "书签颜色")
    private String color;
    @Transient
    @Schema(name = "children", description = "子级")
    protected transient List<BookmarkDomain> children;

    public record CreateRequest(
            @NotBlank(message = "名称不能为空") String name,
            @NotNull(message = "类型不能为空") BookmarkType type,
            @NotBlank(message = "地址不能为空") String url,
            @NotBlank(message = "图标不能为空") String icon,
            @NotBlank(message = "颜色不能为空") String color,
            Long parentId
    ) {
    }

    public record UpdateRequest(
            @NotNull(message = "id不能为空") Long id,
            @NotBlank(message = "名称不能为空") String name,
            @NotNull(message = "类型不能为空") BookmarkType type,
            @NotBlank(message = "地址不能为空") String url,
            @NotBlank(message = "图标不能为空") String icon,
            @NotBlank(message = "颜色不能为空") String color,
            Long parentId
    ) {
    }

    public record GetRequest(@NotNull(message = "id不能为空") Long id) {
    }

    public record DeleteRequest(@NotNull(message = "id不能为空") Long id) {
    }

    public record ListRequest(
            Long id,
            String name,
            BookmarkType type,
            String url
    ) {
    }
}
