package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.domain.EmbeddedRecordDomain;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import com.jinelei.bitterling.web.repository.MemoTagRepository;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@Service
public class MemoTagService extends BaseService<MemoTagRepository, MemoTagDomain, Long> {
    private final MemoTagRelateService memoTagRelateService;

    public MemoTagService(MemoTagRepository repository, Validator validator, MemoTagRelateService memoTagRelateService) {
        super(repository, validator);
        this.memoTagRelateService = memoTagRelateService;
    }

    public List<MemoTagDomain> getTagsByMemoId(Long memoId) {
        Optional.ofNullable(memoId).orElseThrow(() -> new BusinessException("备忘id不能为空"));
        List<MemoTagRelateRecordDomain> memoTagRelateList = memoTagRelateService.findByMemoId(memoId);
        Iterable<MemoTagDomain> tags = repository.findAllById(memoTagRelateList.stream().map(EmbeddedRecordDomain::getId).map(MemoTagPrimaryKey::getTagId).distinct().toList());
        return StreamSupport.stream(tags.spliterator(), false).toList();
    }

}
