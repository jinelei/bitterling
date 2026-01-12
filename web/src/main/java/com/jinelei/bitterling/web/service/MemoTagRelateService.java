package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import com.jinelei.bitterling.web.repository.MemoTagRelateRepository;

import jakarta.validation.Validator;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MemoTagRelateService
        extends BaseService<MemoTagRelateRepository, MemoTagRelateRecordDomain, MemoTagPrimaryKey> {

    public MemoTagRelateService(MemoTagRelateRepository repository,
            Validator validator) {
        super(repository, validator);
    }

    public long deleteByMemoId(Long id) {
        long deleteByMemoId = repository.delete((r, q, cb) -> cb.equal(r.get("id").get("memoId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要删除的memoId不能为空"))));
        log.info("deleteByMemoId: {}", deleteByMemoId);
        return deleteByMemoId;
    }

    public long deleteByTagId(Long id) {
        long deleteByTagId = repository.delete((r, q, cb) -> cb.equal(r.get("id").get("tagId"),
                Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要删除的memoId不能为空"))));
        log.info("deleteByTagId: {}", deleteByTagId);
        return deleteByTagId;
    }
}
