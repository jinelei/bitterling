package com.jinelei.bitterling.service;

import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.MemoTagDomain;
import com.jinelei.bitterling.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.repository.MemoTagRepository;

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
        Iterable<MemoTagDomain> tags = repository.findAllById(memoTagRelateList.stream().map(MemoTagRelateRecordDomain::getTagId).distinct().toList());
        return StreamSupport.stream(tags.spliterator(), false).toList();
    }

}
