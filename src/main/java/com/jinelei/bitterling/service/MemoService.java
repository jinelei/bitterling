package com.jinelei.bitterling.service;

import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.convert.MemoConvertor;
import com.jinelei.bitterling.domain.convert.MemoTagConvertor;
import com.jinelei.bitterling.domain.MemoDomain;
import com.jinelei.bitterling.repository.MemoRepository;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public MemoDomain create(MemoDomain.CreateRequest request) {
        MemoDomain entity = memoConvertor.fromRequest(request);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setOrderNumber(Optional.ofNullable(entity.getOrderNumber()).orElse(0));
        memoTagRelateService.createByMemoId(entity.getId(), request.tagIds());
        return super.save(entity);
    }

    @Transactional
    public MemoDomain update(MemoDomain.UpdateRequest request) {
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

}
