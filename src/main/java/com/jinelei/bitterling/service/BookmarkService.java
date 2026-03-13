package com.jinelei.bitterling.service;

import java.io.IOException;
import java.util.*;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.config.SpringBeanUtils;
import com.jinelei.bitterling.domain.base.RecordDomain;
import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.domain.request.BookmarkCreateRequest;
import com.jinelei.bitterling.domain.request.BookmarkListRequest;
import com.jinelei.bitterling.domain.request.BookmarkUpdateRequest;
import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.utils.ChromeBookmarkUtil;
import com.jinelei.bitterling.utils.ProxyUtil;
import com.jinelei.bitterling.utils.TreeUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.repository.BookmarkRepository;

import jakarta.validation.Validator;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookmarkService extends BaseService<BookmarkRepository, BookmarkDomain, Long> {
    private final BookmarkConvertor bookmarkConvertor;
    private final ChromeBookmarkUtil chromeBookmarkUtil;
    private final ProxyUtil proxyUtil;

    public BookmarkService(BookmarkRepository repository, Validator validator, BookmarkConvertor bookmarkConvertor, ChromeBookmarkUtil chromeBookmarkUtil, ProxyUtil proxyUtil) {
        super(repository, validator);
        this.bookmarkConvertor = bookmarkConvertor;
        this.chromeBookmarkUtil = chromeBookmarkUtil;
        this.proxyUtil = proxyUtil;
    }

    public void save(BookmarkCreateRequest req) {
        BookmarkDomain bookmarkDomain = bookmarkConvertor.fromCreateReq(req);
        bookmarkConvertor.coverParent(bookmarkDomain, bookmarkDomain.getChildren());
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
            Optional.ofNullable(req.name())
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .map(name -> cb.like(r.get("name"), String.format("%%%s%%", name))).ifPresent(list::add);
            Optional.ofNullable(req.url())
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .map(url -> cb.like(r.get("url"), String.format("%%%s%%", url))).ifPresent(list::add);
            Optional.ofNullable(req.keywords())
                    .map(String::trim)
                    .filter(StringUtils::hasLength)
                    .map(keywords -> cb.and(cb.or(
                            cb.like(r.get("name"), String.format("%%%s%%", keywords)),
                            cb.like(r.get("url"), String.format("%%%s%%", keywords))
                    ))).ifPresent(list::add);
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
        List<BookmarkDomain> tree = TreeUtils.convertToTree(list, Comparator.comparingInt(RecordDomain::getOrderNumber));
        return bookmarkConvertor.toResponse(tree);
    }

    public List<BookmarkDomain> parse(MultipartFile file) {
        try {
            ChromeBookmarkUtil.Folder node = chromeBookmarkUtil.parse(file.getInputStream(), "");
            BookmarkCreateRequest from = bookmarkConvertor.from(node);
            SpringBeanUtils.getBean(BookmarkService.class).save(from);
            log.info("根节点: {}", node);
            return List.of();
        } catch (IOException e) {
            log.error("解析书签失败: {}", e.getMessage());
            throw new BusinessException("解析书签失败: " + e.getMessage());
        }
    }

    public Iterable<BookmarkDomain> findTopBookmark(Iterable<BookmarkDomain> all) {
        if (null == all) {
            return null;
        }
        final List<BookmarkDomain> unProxyNode = new ArrayList<>();
        final Set<BookmarkDomain> allNodes = new HashSet<>();
        all.forEach(i -> {
            BookmarkDomain domain = proxyUtil.unProxy(i);
            unProxyNode.add(domain);
            allNodes.addAll(findTopLevel(domain));
        });
        List<BookmarkDomain> top = allNodes.stream().filter(i -> Objects.isNull(i.getParentId())).toList();
        // 剪枝
        final List<BookmarkDomain> temp = new ArrayList<>(top);
        while (!CollectionUtils.isEmpty(temp)) {
            List<BookmarkDomain> t = new ArrayList<>();
            temp.stream().filter(Objects::nonNull).forEach(bookmark -> {
                bookmark.getChildren().removeIf(i -> !allNodes.contains(i));
                t.addAll(bookmark.getChildren());
            });
            temp.clear();
            temp.addAll(t);
        }
        return top;
    }

    public List<BookmarkDomain> findTopLevel(BookmarkDomain bookmark) {
        final List<BookmarkDomain> list = new ArrayList<>();
        if (null == bookmark) {
            return list;
        }
        BookmarkDomain temp = bookmark;
        list.add(temp);
        while (temp.getParent() != null) {
            temp = temp.getParent();
            list.add(temp);
        }
        return list;
    }
}
