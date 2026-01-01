package com.jinelei.bitterling.web.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.jinelei.bitterling.comparator.PriorityComparator;
import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;

@Service
public class BookmarkService extends BaseService<BookmarkDomain, Long> {

    public BookmarkService(BaseRepository<BookmarkDomain, Long> repository) {
        super(repository);
    }

    private final PriorityComparator<String> tagsComparator = PriorityComparator.of(
            new String[]{"全部", "根目录"},
            Comparator.naturalOrder());

    public Map<String, Object> renderBookmark() {
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
        folderNameById.put(null, "全部");
        itemByFolderId.put("全部", map.get(BookmarkType.ITEM));
        props.put("tags", map.get(BookmarkType.FOLDER));
        props.put("bookmarkByTags", itemByFolderId);
        return props;
    }

}
