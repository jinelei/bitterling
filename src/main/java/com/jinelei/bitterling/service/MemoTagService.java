package com.jinelei.bitterling.service;

import com.jinelei.bitterling.exception.BusinessException;
import com.jinelei.bitterling.domain.MemoTagDomain;
import com.jinelei.bitterling.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.repository.MemoTagRepository;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SuppressWarnings("unused")
@Service
public class MemoTagService extends BaseService<MemoTagRepository, MemoTagDomain, Long> {
    private final MemoTagRelateService memoTagRelateService;

    public MemoTagService(MemoTagRepository repository, Validator validator, MemoTagRelateService memoTagRelateService) {
        super(repository, validator);
        this.memoTagRelateService = memoTagRelateService;
    }

    public Map<Long, List<MemoTagDomain>> getTagsByMemoId(List<Long> memoIdList) {
        Optional.ofNullable(memoIdList).filter(c -> !CollectionUtils.isEmpty(c)).orElseThrow(() -> new BusinessException("备忘id不能为空"));
        final Map<Long, List<MemoTagRelateRecordDomain>> memoTagRelateList = memoTagRelateService.findByMemoIdList(memoIdList);
        final Map<Long, Long> tagIdMemoIdMap = memoTagRelateList.values().stream().flatMap(List::stream).collect(Collectors.toMap(MemoTagRelateRecordDomain::getTagId, MemoTagRelateRecordDomain::getMemoId));
        final List<Long> list = memoTagRelateList.values().stream().flatMap(Collection::stream).map(MemoTagRelateRecordDomain::getTagId).toList();
        final Iterable<MemoTagDomain> tags = repository.findAllById(list);
        return StreamSupport.stream(tags.spliterator(), false)
                .filter(i -> tagIdMemoIdMap.containsKey(i.getId()))
                .collect(Collectors.groupingBy(i -> tagIdMemoIdMap.get(i.getId())));
    }

    public List<MemoTagDomain> getTagsByMemoId(Long memoId) {
        Optional.ofNullable(memoId).orElseThrow(() -> new BusinessException("备忘id不能为空"));
        List<MemoTagRelateRecordDomain> memoTagRelateList = memoTagRelateService.findByMemoId(memoId);
        Iterable<MemoTagDomain> tags = repository.findAllById(memoTagRelateList.stream().map(MemoTagRelateRecordDomain::getTagId).distinct().toList());
        return StreamSupport.stream(tags.spliterator(), false).toList();
    }

}
