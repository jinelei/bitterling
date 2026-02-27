package com.jinelei.bitterling.service;

import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.repository.MemoTagRelateRepository;

import jakarta.validation.Validator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@SuppressWarnings("unused")
@Service
public class MemoTagRelateService
        extends BaseService<MemoTagRelateRepository, MemoTagRelateRecordDomain, Long> {

    public MemoTagRelateService(MemoTagRelateRepository repository,
                                Validator validator) {
        super(repository, validator);
    }

    public List<MemoTagRelateRecordDomain> findByMemoId(Long id) {
        List<MemoTagRelateRecordDomain> findByMemoId = repository.findAll((r, q, cb) -> cb.equal(r.get("memoId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要查询的memoId不能为空"))));
        log.info("findByMemoId: {}", findByMemoId);
        return findByMemoId;
    }

    public List<MemoTagRelateRecordDomain> findByTagId(Long id) {
        List<MemoTagRelateRecordDomain> findByTagId = repository.findAll((r, q, cb) -> cb.equal(r.get("tagId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要查询的memoId不能为空"))));
        log.info("findByTagId: {}", findByTagId);
        return findByTagId;
    }

    public long deleteByMemoId(Long id) {
        long deleteByMemoId = repository.delete((r, q, cb) -> cb.equal(r.get("memoId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要删除的memoId不能为空"))));
        log.info("deleteByMemoId: {}", deleteByMemoId);
        return deleteByMemoId;
    }

    public long deleteByTagId(Long id) {
        long deleteByTagId = repository.delete((r, q, cb) -> cb.equal(r.get("tagId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要删除的memoId不能为空"))));
        log.info("deleteByTagId: {}", deleteByTagId);
        return deleteByTagId;
    }

    public void createByMemoId(Long memoId, List<Long> tagIds) {
        List<MemoTagRelateRecordDomain> list = new ArrayList<>();
        Optional.ofNullable(memoId).ifPresent(mid -> {
            List<Long> ids = Optional.ofNullable(tagIds).orElse(new ArrayList<>());
            for (int i = 0; i < ids.size(); i++) {
                MemoTagRelateRecordDomain domain = new MemoTagRelateRecordDomain();
                domain.setMemoId(mid);
                domain.setTagId(ids.get(i));
                domain.setCreateTime(LocalDateTime.now());
                domain.setUpdateTime(LocalDateTime.now());
                domain.setOrderNumber(i);
                list.add(domain);
            }
        });
        if (!CollectionUtils.isEmpty(list)) {
            repository.saveAll(list);
        }
    }
}
