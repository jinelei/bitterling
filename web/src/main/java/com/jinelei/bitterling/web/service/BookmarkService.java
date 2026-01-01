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
            new String[] { "全部", "根目录" },
            Comparator.naturalOrder());

    public Map<String, Object> renderBookmark() {
        final Map<String, Object> props = new HashMap<>();
        Iterable<BookmarkDomain> all = findAll();
        final List<BookmarkDomain> folderList = new ArrayList<>();
        final List<BookmarkDomain> itemList = new ArrayList<>();
        StreamSupport.stream(all.spliterator(), false)
                .forEach(it -> {
                    Optional.ofNullable(it.getType())
                            .filter(BookmarkType.FOLDER::equals)
                            .ifPresentOrElse(i -> {
                                folderList.add(it);
                            }, () -> {
                                itemList.add(it);
                            });
                });
        log.info("null check folderList: {}", folderList);
        log.info("null check itemList: {}", itemList);
        final Map<Long, String> folderNameById = folderList.parallelStream()
                .collect(Collectors.toMap(BookmarkDomain::getId, BookmarkDomain::getName));
        folderNameById.put(null, "全部");
        folderNameById.put(null, "全部");
        log.info("null check folderNameById: {}", folderNameById);
        final Map<String, List<BookmarkDomain>> itemByFolderId = itemList.parallelStream()
                .filter(i -> Objects.nonNull(i.getParentId()))
                .collect(Collectors
                        .groupingBy(i -> folderNameById.get(i.getParentId())));
        itemByFolderId.put("全部", itemList);
        props.put("tags", folderNameById.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(tagsComparator))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new)));
        props.put("bookmarkByTags", itemByFolderId);
        log.info("renderBookmark props: {}", props);
        return props;
    }

}
