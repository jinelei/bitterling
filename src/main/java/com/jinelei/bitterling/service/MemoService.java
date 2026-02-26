package com.jinelei.bitterling.service;

import com.jinelei.bitterling.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.domain.request.MemoCreateRequest;
import com.jinelei.bitterling.domain.request.MemoPageRequest;
import com.jinelei.bitterling.domain.request.MemoUpdateRequest;
import com.jinelei.bitterling.domain.request.PageableRequest;
import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.convert.MemoConvertor;
import com.jinelei.bitterling.domain.convert.MemoTagConvertor;
import com.jinelei.bitterling.domain.MemoDomain;
import com.jinelei.bitterling.repository.MemoRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class MemoService extends BaseService<MemoRepository, MemoDomain, Long> {
    private final MemoTagService memoTagService;
    private final MemoTagRelateService memoTagRelateService;
    private final MemoConvertor memoConvertor;
    private final MemoTagConvertor memoTagConvertor;

    public MemoService(MemoRepository repository, Validator validator, MemoTagService memoTagService,
                       MemoTagRelateService memoTagRelateService, MemoConvertor memoConvertor,
                       MemoTagConvertor memoTagConvertor) {
        super(repository, validator);
        this.memoTagService = memoTagService;
        this.memoTagRelateService = memoTagRelateService;
        this.memoConvertor = memoConvertor;
        this.memoTagConvertor = memoTagConvertor;
    }

    @Transactional
    public MemoDomain create(MemoCreateRequest request) {
        MemoDomain entity = memoConvertor.fromRequest(request);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setOrderNumber(Optional.ofNullable(entity.getOrderNumber()).orElse(0));
        memoTagRelateService.createByMemoId(entity.getId(), request.tagIds());
        return super.save(entity);
    }

    @Transactional
    public MemoDomain update(MemoUpdateRequest request) {
        MemoDomain entity = findById(request.id()).orElseThrow(() -> new BusinessException("备忘未找到"));
        memoConvertor.merge(entity, request);
        entity.setUpdateTime(LocalDateTime.now());
        memoTagRelateService.deleteByMemoId(entity.getId());
        memoTagRelateService.createByMemoId(entity.getId(), request.tagIds());
        return super.save(entity);
    }

    @Transactional
    public void deleteById(Long id) {
        MemoDomain entity = findById(id).orElseThrow(() -> new BusinessException("备忘未找到"));
        super.deleteById(id);
        log.info("删除备忘成功: {}", entity);
        long deleteByMemoId = memoTagRelateService.deleteByMemoId(id);
        log.info("删除备忘标签成功: {}", deleteByMemoId);
    }

    public Page<MemoDomain> page(PageableRequest<MemoPageRequest> req) {
        final Specification<MemoDomain> specification = (r, q, cb) -> {
            final List<Predicate> predicates = new ArrayList<>();
            Optional.ofNullable(req.getQuery())
                    .map(MemoPageRequest::tagId)
                    .map(memoTagRelateService::findByTagId)
                    .map(l -> l.stream().map(MemoTagRelateRecordDomain::getMemoId).toList())
                    .filter(c -> !CollectionUtils.isEmpty(c))
                    .map(list -> r.get("id").in(list))
                    .ifPresent(predicates::add);
            return cb.and(predicates.toArray(Predicate[]::new));
        };
        Page<MemoDomain> all = this.getRepository().findAll(specification, PageRequest.of(Math.max(0, req.getPageNo() - 1), req.getPageSize()));
        return all;
    }
}
