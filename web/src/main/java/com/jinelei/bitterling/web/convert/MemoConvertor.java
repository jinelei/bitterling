package com.jinelei.bitterling.web.convert;

import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Optional;

/**
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 * @version v1.0.0
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface MemoConvertor {
    MutableDataSet options = new MutableDataSet();
    Parser parser = Parser.builder(options).build();
    HtmlRenderer renderer = HtmlRenderer.builder(options).build();

    MemoDomain fromRequest(MemoDomain.CreateRequest r);

    MemoDomain fromRequest(MemoDomain.DeleteRequest r);

    MemoDomain fromRequest(MemoDomain.UpdateRequest r);

    MemoDomain fromRequest(MemoDomain.ListQueryRequest r);

    MemoDomain.DetailResponse toResponse(MemoDomain e);

    MemoDomain merge(@MappingTarget MemoDomain source, MemoDomain.UpdateRequest r);

    default MemoDomain.DetailResponse transTags(@MappingTarget MemoDomain.DetailResponse source, List<MemoTagDomain> tags) {
        return new MemoDomain.DetailResponse(source.id(), source.title(), source.subTitle(), source.content(), source.contentRender(), tags, source.orderNumber(), source.createTime(), source.updateTime());
    }

    default MemoDomain.DetailResponse transContentRender(@MappingTarget MemoDomain.DetailResponse source) {
        final String contentRender = Optional.ofNullable(source)
                .map(MemoDomain.DetailResponse::content)
                .map(s -> renderer.render(parser.parse(s)))
                .orElse("");
        return new MemoDomain.DetailResponse(source.id(), source.title(), source.subTitle(), source.content(), contentRender, source.tags(), source.orderNumber(), source.createTime(), source.updateTime());
    }

}
