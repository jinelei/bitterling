package com.jinelei.bitterling.web.convert;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import com.jinelei.bitterling.web.domain.MemoTagDomain;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface MemoTagConvertor {

    MemoTagDomain.Response toResponse(MemoTagDomain e);

    default MemoTagDomain.CountResponse toCountResponse(@MappingTarget MemoTagDomain.Response target, Long count) {
        return new MemoTagDomain.CountResponse(target.id(), target.title(), target.icon(), count);
    }

}
