package com.jinelei.bitterling.domain;

import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.jinelei.bitterling.domain.base.RecordDomain;
import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import com.jinelei.bitterling.domain.view.TreeView;
import com.jinelei.bitterling.domain.enums.BookmarkType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "BOOKMARK")
@Schema(name = "BookmarkDomain", description = "书签领域对象")
public class BookmarkDomain extends RecordDomain<Long> implements TreeView<BookmarkDomain, Long> {
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
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private BookmarkDomain parent;
    @JsonManagedReference
    @OneToMany(mappedBy = "parent",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true)
    private List<BookmarkDomain> children;

    @Override
    public Long getParentId() {
        return Optional.ofNullable(getParent()).map(RecordDomain::getId).orElse(null);
    }
}
