package com.jinelei.bitterling.web.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;
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

    private final Supplier<String> greetingSupplier = () -> {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        if (hour >= 5 && hour < 9) {
            return "早上好";
        } else if (hour >= 9 && hour < 12) {
            return "上午好";
        } else if (hour >= 12 && hour < 14) {
            return "中午好";
        } else if (hour >= 14 && hour < 18) {
            return "下午好";
        } else if (hour >= 18 && hour < 22) {
            return "晚上好";
        } else {
            return "夜深了";
        }
    };

    public Map<String, Object> indexRenderProperties() {
        log.info("indexRenderProperties");
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
        props.put("greeting", greetingSupplier.get());
        props.put("tags", map.get(BookmarkType.FOLDER));
        props.put("bookmarkByTags", itemByFolderId);
        return props;
    }

}
