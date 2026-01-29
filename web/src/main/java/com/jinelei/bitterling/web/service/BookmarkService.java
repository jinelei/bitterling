package com.jinelei.bitterling.web.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.core.exception.BusinessException;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;
import com.jinelei.bitterling.web.repository.BookmarkRepository;

import jakarta.validation.Validator;

@Service
public class BookmarkService extends BaseService<BookmarkRepository, BookmarkDomain, Long> {

    public BookmarkService(BookmarkRepository repository, Validator validator) {
        super(repository, validator);
    }

    public Map<String, Object> renderIndex() {
        final Map<String, Object> props = new HashMap<>();
        Iterable<BookmarkDomain> all = findAll();
        final Map<BookmarkType, List<BookmarkDomain>> map = StreamSupport.stream(all.spliterator(), true)
                .filter(i -> Objects.nonNull(i.getType()))
                .collect(Collectors.groupingBy(BookmarkDomain::getType));
        final Map<Long, String> folderNameById = map.getOrDefault(BookmarkType.FOLDER, new ArrayList<>())
                .parallelStream()
                .collect(Collectors.toMap(BookmarkDomain::getId, BookmarkDomain::getName));
        final Map<String, List<BookmarkDomain>> itemByFolderId = map.getOrDefault(BookmarkType.ITEM, new ArrayList<>())
                .parallelStream()
                .filter(i -> Objects.nonNull(i.getParentId()))
                .collect(Collectors.groupingBy(i -> folderNameById.get(i.getParentId())));
        itemByFolderId.put("全部", map.get(BookmarkType.ITEM));
        props.put("tags", map.get(BookmarkType.FOLDER));
        props.put("bookmarkByTags", itemByFolderId);
        return props;
    }

    public Iterable<BookmarkDomain> myFavoriteBookmarks() {
        Optional<BookmarkDomain> one = repository.findOne((Specification<BookmarkDomain>) (r, q, cb) -> cb.and(
                cb.equal(r.get("name"), "收藏"),
                cb.equal(r.get("type"), BookmarkType.FOLDER)
        ));
        BookmarkDomain favorite = one.orElseThrow(() -> new BusinessException("未找到个人收藏"));
        List<BookmarkDomain> all = repository.findAll((Specification<BookmarkDomain>) (r, q, cb) -> cb.and(
                cb.equal(r.get("parentId"), favorite.getId()),
                cb.equal(r.get("type"), BookmarkType.ITEM)
        ));
        return all;
    }
}
