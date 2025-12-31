package com.jinelei.bitterling.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;

@Service
public class BookmarkService extends BaseService<BookmarkDomain, Long> {

    public BookmarkService(BaseRepository<BookmarkDomain, Long> repository) {
        super(repository);
    }

    public Map<String, Object> renderBookmark() {
        final Map<String, Object> props = new HashMap<>();
        Iterable<BookmarkDomain> all = findAll();
        final List<BookmarkDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        final List<BookmarkDomain> folderList = list.parallelStream()
                .filter(i -> Objects.nonNull(i.getId()))
                .filter(i -> Objects.nonNull(i.getType()))
                .filter(i -> i.getType() == BookmarkType.FOLDER)
                .distinct()
                .sorted()
                .toList();
        final Map<Long, String> folderNameById = folderList.parallelStream()
                .collect(Collectors.toMap(BookmarkDomain::getId, BookmarkDomain::getName));
        final Map<String, List<BookmarkDomain>> itemByFolderId = list.parallelStream()
                .filter(i -> Objects.nonNull(i.getParentId()))
                .filter(i -> i.getType() == BookmarkType.ITEM)
                .collect(Collectors
                        .groupingBy(i -> Optional.ofNullable(i.getParentId()).map(folderNameById::get).orElse("根目录")));
        itemByFolderId.put("全部", list);
        props.put("tags", itemByFolderId.keySet().stream().sorted((String o1, String o2) -> {
            if ("全部".equals(o1)) {
                return -1;
            } else if ("全部".equals(o2)) {
                return 1;
            } else {
                return o1.compareTo(o2);
            }
        }).toList());
        props.put("bookmarkByTags", itemByFolderId);
        log.info("renderBookmark props: {}", props);
        return props;
    }

}
