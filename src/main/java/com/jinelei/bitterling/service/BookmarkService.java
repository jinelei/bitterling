package com.jinelei.bitterling.service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.utils.TreeUtils;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.enums.BookmarkType;
import com.jinelei.bitterling.repository.BookmarkRepository;

import jakarta.validation.Validator;
import org.springframework.util.CollectionUtils;

@Service
public class BookmarkService extends BaseService<BookmarkRepository, BookmarkDomain, Long> {
    private final BookmarkConvertor bookmarkConvertor;

    public BookmarkService(BookmarkRepository repository, Validator validator, BookmarkConvertor bookmarkConvertor) {
        super(repository, validator);
        this.bookmarkConvertor = bookmarkConvertor;
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

    public void sort(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        Iterable<BookmarkDomain> allById = getRepository().findAllById(ids);
        List<BookmarkDomain> list = StreamSupport.stream(allById.spliterator(), false)
                .peek(it -> {
                    it.setOrderNumber(ids.indexOf(it.getId()));
                })
                .toList();
        getRepository().saveAll(list);
    }

    public List<BookmarkDomain> tree() {
        Iterable<BookmarkDomain> all = findAll();
        List<BookmarkDomain> list = new ArrayList<>();
        StreamSupport.stream(all.spliterator(), false).forEach(list::add);
        List<BookmarkDomain> tree = TreeUtils.convertToTree(list, Comparator.comparingInt(TreeRecordDomain::getOrderNumber));
        return tree;
    }
}
