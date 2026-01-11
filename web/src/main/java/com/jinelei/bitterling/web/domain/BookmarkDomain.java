package com.jinelei.bitterling.web.domain;

import java.util.List;

import com.jinelei.bitterling.core.domain.TreeRecordDomain;
import com.jinelei.bitterling.core.domain.view.TreeView;
import com.jinelei.bitterling.web.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BOOKMARK")
@Schema(title = "书签领域对象", description = "书签领域对象")
public class BookmarkDomain extends TreeRecordDomain<Long> implements TreeView<BookmarkDomain, Long> {
    @Column(name = "name", unique = true)
    @Schema(description = "书签名称")
    private String name;
    @Column(name = "type")
    @Schema(description = "书签类型")
    private BookmarkType type;
    @Column(name = "url")
    @Schema(description = "书签地址")
    private String url;
    @Column(name = "icon")
    @Schema(description = "书签图标")
    private String icon;
    @Column(name = "color")
    @Schema(description = "书签颜色")
    private String color;
    @Transient
    @Schema(description = "子级")
    protected transient List<BookmarkDomain> children;
}
