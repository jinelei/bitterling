package com.jinelei.bitterling.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.exception.BusinessException;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.enums.BookmarkType;
import com.jinelei.bitterling.repository.BookmarkRepository;

import jakarta.validation.Validator;

@Service
public class BookmarkService extends BaseService<BookmarkRepository, BookmarkDomain, Long> {
    private final BookmarkConvertor bookmarkConvertor;

    public BookmarkService(BookmarkRepository repository, Validator validator, BookmarkConvertor bookmarkConvertor) {
        super(repository, validator);
        this.bookmarkConvertor = bookmarkConvertor;
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

    public void save(BookmarkDomain.CreateRequest req) {
        BookmarkDomain bookmarkDomain = bookmarkConvertor.fromCreateReq(req);
        repository.save(bookmarkDomain);
    }

    public void update(BookmarkDomain.UpdateRequest req) {
        BookmarkDomain exist = repository.findById(req.id()).orElseThrow(() -> new BusinessException("未找到书签"));
        BookmarkDomain merge = bookmarkConvertor.merge(exist, req);
        repository.save(merge);
    }

    public Iterable<BookmarkDomain> findList(BookmarkDomain.ListRequest req) {
        Optional.ofNullable(req).orElseThrow(() -> new BusinessException("请求不能为空"));
        return repository.findAll((Specification<BookmarkDomain>) (r, q, cb) -> {
            List<Predicate> list = new ArrayList<>();
            Optional.ofNullable(req.id())
                    .map(id -> cb.equal(r.get("id"), id))
                    .ifPresent(list::add);
            Optional.ofNullable(req.type())
                    .map(type -> cb.equal(r.get("type"), type))
                    .ifPresent(list::add);
            Optional.ofNullable(req.name())
                    .map(name -> cb.like(r.get("name"), String.format("%%%s%%", name)))
                    .ifPresent(list::add);
            Optional.ofNullable(req.url())
                    .map(url -> cb.like(r.get("url"), String.format("%%%s%%", url)))
                    .ifPresent(list::add);
            return cb.and(list.toArray(new Predicate[0]));
        });
    }
}
