package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoDomain;
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

    public Map<String, Object> renderIndex() {
        final Map<String, Object> props = new HashMap<>();
        Iterable<MemoDomain> all = findAll();
        List<MemoDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        props.put("memoList", list);
        log.info("renderIndex: {}", props);
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

    public void mock() {
        List<MemoDomain> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MemoDomain domain = new MemoDomain();
            domain.setId((long) i);
            domain.setTitle("标题" + i);
            domain.setTitle("副标题" + i);
            domain.setContent("副标题1" + i);
            domain.setCreateTime(LocalDateTime.now());
            domain.setUpdateTime(LocalDateTime.now());
            list.add(domain);
        }
        saveAll(list);
    }
}
