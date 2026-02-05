package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.BookmarkDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@SuppressWarnings("unused")
@Mapper(componentModel = "spring")
public interface BookmarkConvertor {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    BookmarkDomain fromCreateReq(BookmarkDomain.CreateRequest r);

    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    BookmarkDomain merge(@MappingTarget BookmarkDomain source, BookmarkDomain.UpdateRequest r);

}
