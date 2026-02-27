package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.MemoDomain;
import com.jinelei.bitterling.domain.MemoTagDomain;
import com.jinelei.bitterling.domain.request.MemoCreateRequest;
import com.jinelei.bitterling.domain.request.MemoUpdateRequest;
import com.jinelei.bitterling.domain.response.MemoResponse;
import com.jinelei.bitterling.domain.response.MemoTagResponse;
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

    MemoResponse toResponse(MemoDomain e);

    List<MemoResponse> toResponse(List<MemoDomain> e);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    void merge(@MappingTarget MemoDomain source, MemoUpdateRequest r);

    default void merge(@MappingTarget MemoDomain source, List<MemoTagDomain> tags) {
        source.setTags(tags);
    }

}
