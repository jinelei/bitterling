package com.jinelei.bitterling.web.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;

import jakarta.validation.Validator;

@Service
public class BookmarkService extends BaseService<BookmarkDomain, Long> {

    public BookmarkService(BaseRepository<BookmarkDomain, Long> repository, Validator validator) {
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
        log.info("renderIndex: {}", props);
        return props;
    }

}
