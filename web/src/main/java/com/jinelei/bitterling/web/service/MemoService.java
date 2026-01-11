package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.domain.EmbeddedRecordDomain;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.helper.LongIdGenerator;
import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.dto.MemoPageRequest;
import com.jinelei.bitterling.web.domain.dto.TagDto;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class MemoService extends BaseService<MemoDomain, Long> {
    private final MemoTagService memoTagService;
    private final MemoTagRelateService memoTagRelateService;
    private final Parser parser;
    private final HtmlRenderer renderer;
    private final LongIdGenerator idGenerator = new LongIdGenerator();

    public MemoService(BaseRepository<MemoDomain, Long> repository, Validator validator, MemoTagService memoTagService, MemoTagRelateService memoTagRelateService) {
        super(repository, validator);
        this.memoTagService = memoTagService;
        this.memoTagRelateService = memoTagRelateService;
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    public Map<String, Object> renderIndex(MemoPageRequest request) {
        final Map<String, Object> props = new HashMap<>();
        final Map<Long, MemoTagDomain> tagById = StreamSupport.stream(memoTagService.findAll().spliterator(), true)
                .filter(i -> Objects.nonNull(i.getId()))
                .filter(i -> Objects.nonNull(i.getTitle()))
                .collect(Collectors.toMap(MemoTagDomain::getId, i -> i));
        final List<MemoTagRelateRecordDomain> originMemoTagRelateList = StreamSupport.stream(memoTagRelateService.findAll().spliterator(), true)
                .filter(i -> Optional.ofNullable(i.getId()).map(MemoTagPrimaryKey::getMemoId).isPresent())
                .filter(i -> Optional.ofNullable(i.getId()).map(MemoTagPrimaryKey::getTagId).isPresent())
                .toList();
        final Map<Long, List<MemoTagDomain>> memoTagListById = originMemoTagRelateList.parallelStream()
                .collect(Collectors.groupingBy(i -> i.getId().getMemoId(), Collectors.mapping(l -> tagById.get(l.getId().getTagId()), Collectors.toList())));
        final List<MemoDomain> memoList = repository.findAll((Specification<MemoDomain>) (r, q, cb) -> {
                    final List<Predicate> predicates = new ArrayList<>();
                    final List<Long> ids = new ArrayList<>();
                    Optional.ofNullable(request).map(MemoPageRequest::getId).ifPresent(ids::add);
                    Optional.ofNullable(request).map(MemoPageRequest::getTagId)
                            .map(id -> memoTagRelateService.getRepository().findAll((Specification<MemoTagRelateRecordDomain>) (r1, q1, cb1) -> cb1.equal(r1.get("id").get("tagId"), id)))
                            .stream()
                            .flatMap(List::stream)
                            .map(MemoTagRelateRecordDomain::getId)
                            .map(MemoTagPrimaryKey::getMemoId)
                            .distinct()
                            .forEach(ids::add);
                    if (Optional.ofNullable(request).map(MemoPageRequest::getId).isPresent() || Optional.ofNullable(request).map(MemoPageRequest::getTagId).isPresent()) {
                        Optional.of(ids)
                                .filter(l -> !l.isEmpty())
                                .map(l -> r.get("id").in(l))
                                .ifPresentOrElse(predicates::add, cb::disjunction);
                    }
                    return cb.and(predicates.toArray(Predicate[]::new));
                }).stream()
                .peek(it -> Optional.of(memoTagListById.get(it.getId())).ifPresent(it::setTags))
                .toList();
        props.put("memoList", memoList);
        final Map<Long, Long> memoCountByTagId = originMemoTagRelateList.parallelStream()
                .collect(Collectors.groupingBy(i -> i.getId().getMemoId(), Collectors.counting()));
        final List<MemoTagDomain> tagList = StreamSupport.stream(memoTagService.findAll().spliterator(), true)
                .sorted()
                .peek(i -> i.setCount(memoCountByTagId.getOrDefault(i.getId(), 0L)))
                .toList();
        props.put("tagList", tagList);
        props.put("currentTagId", request.getTagId());
        log.info("renderIndex: {}", props);
        return props;
    }

    public Map<String, Object> renderDetail(MemoPageRequest request) {
        final Map<String, Object> props = new HashMap<>();
        Optional<MemoDomain> optById = repository.findById(Optional.ofNullable(request).map(MemoPageRequest::getId).orElseThrow(() -> new BusinessException("id不能为空")));
        final MemoDomain memo = optById.orElseThrow(() -> new BusinessException("备忘不能为空"));
        props.put("memo", memo);
        props.put("tagList", List.of(
                new TagDto(1L, "fa-briefcase", "工作", (int) Math.round(Math.random() * 10)),
                new TagDto(2L, "fa-home", "生活", (int) Math.round(Math.random() * 10)),
                new TagDto(3L, "fa-book", "学习", (int) Math.round(Math.random() * 10))
        ));
        log.info("renderDetail: {}", props);
        return props;
    }


    public Map<String, ?> renderCreate() {
        final Map<String, Object> props = new HashMap<>();
        Iterable<MemoDomain> all = findAll();
        List<MemoDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        props.put("memoList", list);
        props.put("tagList", List.of(
                new TagDto(1L, "fa-briefcase", "工作", (int) Math.round(Math.random() * 10)),
                new TagDto(2L, "fa-home", "生活", (int) Math.round(Math.random() * 10)),
                new TagDto(3L, "fa-book", "学习", (int) Math.round(Math.random() * 10))
        ));
        log.info("renderCreate: {}", props);
        return props;
    }

    public MemoDomain renderContent(MemoDomain domain) {
        Optional.ofNullable(domain).ifPresent(i -> {
            Optional.ofNullable(i.getContent()).ifPresent(j -> {
                if (j == null || j.isBlank()) {
                    return;
                }
                Node document = parser.parse(j);
                i.setContent(renderer.render(document));
            });
        });
        return domain;
    }

    @Override
    public MemoDomain save(MemoDomain entity) {
        entity.setId(idGenerator.generateId());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return super.save(entity);
    }

}
