package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.dto.MemoPageRequest;
import com.jinelei.bitterling.web.domain.dto.TagDto;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.StreamSupport;

@Service
public class MemoService extends BaseService<MemoDomain, Long> {
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MemoService(BaseRepository<MemoDomain, Long> repository, Validator validator) {
        super(repository, validator);
        MutableDataSet options = new MutableDataSet();
        parser = Parser.builder(options).build();
        renderer = HtmlRenderer.builder(options).build();
    }

    public Map<String, Object> renderIndex(MemoPageRequest request) {
        final Map<String, Object> props = new HashMap<>();
        Iterable<MemoDomain> all = findAll();
        List<MemoDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        props.put("memoList", list);
        props.put("tagList", List.of(
                new TagDto(1L, "fa-briefcase", "工作", (int) Math.round(Math.random() * 10)),
                new TagDto(2L, "fa-home", "生活", (int) Math.round(Math.random() * 10)),
                new TagDto(3L, "fa-book", "学习", (int) Math.round(Math.random() * 10))
        ));
        log.info("renderIndex: {}", props);
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
}
