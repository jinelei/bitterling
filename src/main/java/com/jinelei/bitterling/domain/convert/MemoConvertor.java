package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.base.RecordDomain;
import com.jinelei.bitterling.domain.MemoDomain;
import com.jinelei.bitterling.domain.MemoTagDomain;
import com.jinelei.bitterling.domain.request.MemoCreateRequest;
import com.jinelei.bitterling.domain.request.MemoUpdateRequest;
import com.jinelei.bitterling.domain.response.MemoResponse;
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
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    MemoDomain fromRequest(MemoCreateRequest r);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "tagIds", ignore = true)
    MemoResponse toResponse(MemoDomain e);

    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "tagIds", ignore = true)
    List<MemoResponse> toResponse(List<MemoDomain> e);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    MemoDomain merge(@MappingTarget MemoDomain source, MemoUpdateRequest r);

    default MemoResponse transTags(@MappingTarget MemoResponse source,
                                   List<MemoTagDomain> tags) {
        final List<Long> tagIds = Optional.ofNullable(tags)
                .map(l -> l.stream().map(RecordDomain::getId).toList())
                .orElse(new ArrayList<>());
        return new MemoResponse(source.id(), source.createTime(),
                source.updateTime(), source.orderNumber(),
                source.title(), source.subTitle(), source.content(),
                tags, tagIds);
    }

}
