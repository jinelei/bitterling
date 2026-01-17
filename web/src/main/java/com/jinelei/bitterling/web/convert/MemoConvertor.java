package com.jinelei.bitterling.web.convert;

import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.vladsch.flexmark.ext.tables.TablesExtension;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.*;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface MemoConvertor {
    MutableDataSet options = new MutableDataSet() {
        {
            set(Parser.EXTENSIONS, List.of(TablesExtension.create()));
            set(TablesExtension.COLUMN_SPANS, true);
            set(TablesExtension.MIN_HEADER_ROWS, 1);
            set(TablesExtension.MAX_HEADER_ROWS, 1);
            set(TablesExtension.APPEND_MISSING_COLUMNS, true);
            set(TablesExtension.DISCARD_EXTRA_COLUMNS, true);
            set(TablesExtension.HEADER_SEPARATOR_COLUMN_MATCH, true);
        }
    };
    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    MemoDomain fromRequest(MemoDomain.CreateRequest r);

    @Mapping(target = "contentRender", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "tagIds", ignore = true)
    MemoDomain.DetailResponse toResponse(MemoDomain e);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    MemoDomain merge(@MappingTarget MemoDomain source, MemoDomain.UpdateRequest r);

    default MemoDomain.DetailResponse transTags(@MappingTarget MemoDomain.DetailResponse source,
                                                List<MemoTagDomain> tags) {
        final List<Long> tagIds = Optional.ofNullable(tags)
                .map(l -> l.stream().map(RecordDomain::getId).toList())
                .orElse(new ArrayList<>());
        return new MemoDomain.DetailResponse(source.id(), source.title(), source.subTitle(), source.content(),
                source.contentRender(), tags, tagIds, source.orderNumber(), source.createTime(),
                source.updateTime());
    }

    default MemoDomain.DetailResponse transContentRender(@MappingTarget MemoDomain.DetailResponse source) {
        if (Objects.isNull(source)) {
            return null;
        }
        final String contentRender = Optional.of(source)
                .map(MemoDomain.DetailResponse::content)
                .map(s -> renderer.render(parser.parse(s)))
                .orElse("");
        return new MemoDomain.DetailResponse(source.id(), source.title(), source.subTitle(), source.content(),
                contentRender, source.tags(), source.tagIds(), source.orderNumber(),
                source.createTime(),
                source.updateTime());
    }

}
