package com.jinelei.bitterling.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.domain.request.BookmarkCreateRequest;
import com.jinelei.bitterling.domain.request.BookmarkListRequest;
import com.jinelei.bitterling.domain.request.BookmarkUpdateRequest;
import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.utils.ChromeBookmarkUtil;
import com.jinelei.bitterling.utils.TreeUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.repository.BookmarkRepository;

import jakarta.validation.Validator;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookmarkService extends BaseService<BookmarkRepository, BookmarkDomain, Long> {
    private final BookmarkConvertor bookmarkConvertor;
    private final ChromeBookmarkUtil chromeBookmarkUtil;

    public BookmarkService(BookmarkRepository repository, Validator validator, BookmarkConvertor bookmarkConvertor, ChromeBookmarkUtil chromeBookmarkUtil) {
        super(repository, validator);
        this.bookmarkConvertor = bookmarkConvertor;
        this.chromeBookmarkUtil = chromeBookmarkUtil;
    }

    public void save(BookmarkCreateRequest req) {
        BookmarkDomain bookmarkDomain = bookmarkConvertor.fromCreateReq(req);
        repository.save(bookmarkDomain);
    }

    public void update(BookmarkUpdateRequest req) {
        BookmarkDomain exist = repository.findById(req.id()).orElseThrow(() -> new BusinessException("未找到书签"));
        bookmarkConvertor.merge(exist, req);
        repository.save(exist);
    }

    public Iterable<BookmarkDomain> findList(BookmarkListRequest req) {
        Optional.ofNullable(req).orElseThrow(() -> new BusinessException("请求不能为空"));
        return repository.findAll((Specification<BookmarkDomain>) (r, q, cb) -> {
            List<Predicate> list = new ArrayList<>();
            Optional.ofNullable(req.id()).map(id -> cb.equal(r.get("id"), id)).ifPresent(list::add);
            Optional.ofNullable(req.type()).map(type -> cb.equal(r.get("type"), type)).ifPresent(list::add);
            Optional.ofNullable(req.name()).map(name -> cb.like(r.get("name"), String.format("%%%s%%", name))).ifPresent(list::add);
            Optional.ofNullable(req.url()).map(url -> cb.like(r.get("url"), String.format("%%%s%%", url))).ifPresent(list::add);
            return cb.and(list.toArray(new Predicate[0]));
        });
    }

    public void sort(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return;
        }
        Iterable<BookmarkDomain> allById = getRepository().findAllById(ids);
        List<BookmarkDomain> list = StreamSupport.stream(allById.spliterator(), false).peek(it -> it.setOrderNumber(ids.indexOf(it.getId()))).toList();
        getRepository().saveAll(list);
    }

    public List<BookmarkResponse> tree() {
        Iterable<BookmarkDomain> all = findAll();
        List<BookmarkDomain> list = new ArrayList<>();
        StreamSupport.stream(all.spliterator(), false).forEach(list::add);
        List<BookmarkDomain> tree = TreeUtils.convertToTree(list, Comparator.comparingInt(TreeRecordDomain::getOrderNumber));
        return bookmarkConvertor.toResponse(tree);
    }

    public List<BookmarkDomain> parse(MultipartFile file) {
        try {
            ChromeBookmarkUtil.Folder node = chromeBookmarkUtil.parse(file.getInputStream(), "");
            log.info("根节点: {}", node);
            return List.of();
        } catch (IOException e) {
            log.error("解析书签失败: {}", e.getMessage());
            throw new BusinessException("解析书签失败: " + e.getMessage());
        }
    }
}
