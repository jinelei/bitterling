package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import jakarta.validation.Validator;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class MemoTagRelateService extends BaseService<MemoTagRelateRecordDomain, MemoTagPrimaryKey> {

    public MemoTagRelateService(BaseRepository<MemoTagRelateRecordDomain, MemoTagPrimaryKey> repository,
            Validator validator) {
        super(repository, validator);
    }

    public void deleteByMemoId(Long id) {
        Optional.ofNullable(id).orElseThrow(() -> new BusinessException("需要删除的备忘id不能为空"));
        long delete = repository.delete((r, q, cb) -> cb.equal(r.get("memoId"), id));
        log.info("通过memoId删除成功: {}", delete);
    }

}
