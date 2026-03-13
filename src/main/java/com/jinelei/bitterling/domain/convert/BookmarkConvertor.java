package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import com.jinelei.bitterling.domain.request.BookmarkCreateRequest;
import com.jinelei.bitterling.domain.request.BookmarkUpdateRequest;
import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.utils.ChromeBookmarkUtil;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BookmarkConvertor {

    @Mapping(source = "parentId", target = "parentId")
    void createMapTree(@MappingTarget TreeRecordDomain<Long> tree, BookmarkCreateRequest source);

    @Mapping(source = "parentId", target = "parentId")
    @Mapping(source = "id", target = "id")
    void updateMapTree(@MappingTarget TreeRecordDomain<Long> tree, BookmarkUpdateRequest source);

    @InheritConfiguration(name = "createMapTree")
    BookmarkDomain fromCreateReq(BookmarkCreateRequest r);

    @InheritConfiguration(name = "updateMapTree")
    void merge(@MappingTarget BookmarkDomain source, BookmarkUpdateRequest r);

    BookmarkResponse toResponse(BookmarkDomain e);

    List<BookmarkResponse> toResponse(List<BookmarkDomain> e);

    default BookmarkCreateRequest coverChildren(BookmarkCreateRequest request, List<BookmarkCreateRequest> children) {
        if (request == null) {
            return null;
        }
        return new BookmarkCreateRequest(request.name(), request.type(), request.url(), request.icon(), request.color(), null, children);
    }

    @Mapping(target = "type", constant = "FOLDER")
    BookmarkCreateRequest fromFolder(ChromeBookmarkUtil.Folder folder);

    @Mapping(target = "type", constant = "BOOKMARK")
    BookmarkCreateRequest fromBookmark(ChromeBookmarkUtil.Bookmark bookmark);

    default BookmarkCreateRequest from(ChromeBookmarkUtil.Base base) {
        if (base == null) {
            return null;
        }
        List<BookmarkCreateRequest> list = new ArrayList<>();
        if (base instanceof ChromeBookmarkUtil.Folder folder) {
            BookmarkCreateRequest rr = fromFolder(folder);
            if (!CollectionUtils.isEmpty(folder.getChildren())) {
                List<BookmarkCreateRequest> from = from(folder.getChildren());
                return coverChildren(rr, from);
            }
            return rr;
        }
        if (base instanceof ChromeBookmarkUtil.Bookmark bookmark) {
            return fromBookmark(bookmark);
        }
        return null;
    }

    default List<BookmarkCreateRequest> from(List<ChromeBookmarkUtil.Base> list) {
        if (list == null) {
            return null;
        }
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        final List<BookmarkCreateRequest> result = new ArrayList<>();
        list.stream().map(this::from).forEach(result::add);
        return result;
    }

    default void coverParent(BookmarkDomain bookmarkDomain, List<BookmarkDomain> children) {
        if (bookmarkDomain == null) {
            return;
        }
        if (CollectionUtils.isEmpty(children)) {
            return;
        }
        children.forEach(child -> {
            child.setParent(bookmarkDomain);
            coverParent(child, child.getChildren());
        });
    }
}
