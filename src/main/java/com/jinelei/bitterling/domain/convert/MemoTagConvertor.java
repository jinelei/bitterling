package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.response.MemoTagResponse;
import org.mapstruct.Mapper;

import com.jinelei.bitterling.domain.MemoTagDomain;

import java.util.List;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface MemoTagConvertor {

    MemoTagResponse toResponse(MemoTagDomain e);

    List<MemoTagResponse> toResponse(List<MemoTagDomain> e);

}
