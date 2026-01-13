package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.helper.LongIdGenerator;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.convert.MemoConvertor;
import com.jinelei.bitterling.web.convert.MemoTagConvertor;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.dto.MemoPageRequest;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import com.jinelei.bitterling.web.repository.MemoRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class MemoService extends BaseService<MemoRepository, MemoDomain, Long> {
        private final MemoTagService memoTagService;
        private final MemoTagRelateService memoTagRelateService;
        private final MemoConvertor memoConvertor;
        private final MemoTagConvertor memoTagConvertor;
        private final LongIdGenerator idGenerator = new LongIdGenerator();

        public MemoService(MemoRepository repository, Validator validator, MemoTagService memoTagService,
                        MemoTagRelateService memoTagRelateService, MemoConvertor memoConvertor,
                        MemoTagConvertor memoTagConvertor) {
                super(repository, validator);
                this.memoTagService = memoTagService;
                this.memoTagRelateService = memoTagRelateService;
                this.memoConvertor = memoConvertor;
                this.memoTagConvertor = memoTagConvertor;
        }

        public Map<String, Object> renderIndex(MemoPageRequest request) {
                final Map<String, Object> props = new HashMap<>();
                final Map<Long, MemoTagDomain> tagById = StreamSupport
                                .stream(memoTagService.findAll().spliterator(), true)
                                .filter(i -> Objects.nonNull(i.getId()))
                                .filter(i -> Objects.nonNull(i.getTitle()))
                                .collect(Collectors.toMap(MemoTagDomain::getId, i -> i));
                final List<MemoTagRelateRecordDomain> originMemoTagRelateList = StreamSupport
                                .stream(memoTagRelateService.findAll().spliterator(), true)
                                .filter(i -> Optional.ofNullable(i.getId()).map(MemoTagPrimaryKey::getMemoId)
                                                .isPresent())
                                .filter(i -> Optional.ofNullable(i.getId()).map(MemoTagPrimaryKey::getTagId)
                                                .isPresent())
                                .toList();
                final Map<Long, List<MemoTagDomain>> memoTagListById = originMemoTagRelateList.parallelStream()
                                .collect(Collectors.groupingBy(i -> i.getId().getMemoId(),
                                                Collectors.mapping(l -> tagById.get(l.getId().getTagId()),
                                                                Collectors.toList())));
                final List<MemoDomain.DetailResponse> memoList = repository
                                .findAll((Specification<MemoDomain>) (r, q, cb) -> {
                                        final List<Predicate> predicates = new ArrayList<>();
                                        final List<Long> ids = new ArrayList<>();
                                        Optional.ofNullable(request).map(MemoPageRequest::getId).ifPresent(ids::add);
                                        Optional.ofNullable(request).map(MemoPageRequest::getTagId)
                                                        .map(id -> memoTagRelateService.getRepository()
                                                                        .findAll((Specification<MemoTagRelateRecordDomain>) (
                                                                                        r1, q1, cb1) -> cb1
                                                                                                        .equal(r1.get("id")
                                                                                                                        .get("tagId"),
                                                                                                                        id)))
                                                        .stream()
                                                        .flatMap(List::stream)
                                                        .map(MemoTagRelateRecordDomain::getId)
                                                        .map(MemoTagPrimaryKey::getMemoId)
                                                        .distinct()
                                                        .forEach(ids::add);
                                        if (Optional.ofNullable(request).map(MemoPageRequest::getId).isPresent()
                                                        || Optional.ofNullable(request).map(MemoPageRequest::getTagId)
                                                                        .isPresent()) {
                                                Optional.of(ids)
                                                                .filter(l -> !l.isEmpty())
                                                                .map(l -> r.get("id").in(l))
                                                                .ifPresentOrElse(predicates::add, cb::disjunction);
                                        }
                                        return cb.and(predicates.toArray(Predicate[]::new));
                                }).stream()
                                .map(memoConvertor::toResponse)
                                .map(it -> memoConvertor.transTags(it,
                                                memoTagListById.getOrDefault(it.id(), new ArrayList<>())))
                                .toList();
                props.put("memoList", memoList);
                final Map<Long, Long> memoCountByTagId = originMemoTagRelateList.parallelStream()
                                .collect(Collectors.groupingBy(i -> i.getId().getTagId(), Collectors.counting()));
                final List<MemoTagDomain.CountResponse> tagList = StreamSupport
                                .stream(memoTagService.findAll().spliterator(), true)
                                .map(memoTagConvertor::toResponse)
                                .map(t -> memoTagConvertor.toCountResponse(t,
                                                memoCountByTagId.getOrDefault(t.id(), 0L)))
                                .toList();
                props.put("tagList", tagList);
                props.put("currentTagId", request.getTagId());
                log.info("renderIndex: {}", props);
                return props;
        }

        public Map<String, Object> renderDetail(MemoPageRequest request) {
                final Map<String, Object> props = new HashMap<>();
                final Optional<MemoDomain> optById = Optional.ofNullable(request).map(MemoPageRequest::getId)
                                .map(repository::findById)
                                .orElseThrow(() -> new BusinessException("id不能为空"));
                MemoDomain.DetailResponse memo = optById.map(memoConvertor::toResponse)
                                .orElseThrow(() -> new BusinessException("备忘不能为空"));
                final List<MemoTagDomain> tagsByMemoId = memoTagService.getTagsByMemoId(memo.id());
                memo = memoConvertor.transTags(memo, tagsByMemoId);
                memo = memoConvertor.transContentRender(memo);
                props.put("memo", memo);
                final List<MemoTagDomain> tagList = StreamSupport.stream(memoTagService.findAll().spliterator(), true)
                                .sorted(Comparator.comparingInt(RecordDomain::getOrderNumber)).toList();
                props.put("tagList", tagList);
                return props;
        }

        public Map<String, ?> renderCreate() {
                final Map<String, Object> props = new HashMap<>();
                MemoDomain.DetailResponse memo = memoConvertor.toResponse(new MemoDomain());
                memo = memoConvertor.transTags(memo, new ArrayList<>());
                memo = memoConvertor.transContentRender(memo);
                props.put("memo", memo);
                final List<MemoTagDomain> tagList = StreamSupport.stream(memoTagService.findAll().spliterator(), true)
                                .sorted(Comparator.comparingInt(RecordDomain::getOrderNumber)).toList();
                props.put("tagList", tagList);
                return props;
        }

        @Transactional
        public MemoDomain create(MemoDomain.CreateRequest request) {
                MemoDomain entity = memoConvertor.fromRequest(request);
                entity.setCreateTime(LocalDateTime.now());
                entity.setUpdateTime(LocalDateTime.now());
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
