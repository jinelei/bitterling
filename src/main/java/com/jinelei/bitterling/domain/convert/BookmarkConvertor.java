package com.jinelei.bitterling.domain.convert;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.base.TreeRecordDomain;
import org.mapstruct.InheritConfiguration;
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

    @Mapping(source = "parentId", target = "parentId")
    void createMapTree(@MappingTarget TreeRecordDomain<Long> tree, BookmarkDomain.CreateRequest source);

    @Mapping(source = "parentId", target = "parentId")
    @Mapping(source = "id", target = "id")
    void updateMapTree(@MappingTarget TreeRecordDomain<Long> tree, BookmarkDomain.UpdateRequest source);

    @InheritConfiguration(name = "createMapTree")
    BookmarkDomain fromCreateReq(BookmarkDomain.CreateRequest r);

    @InheritConfiguration(name = "updateMapTree")
    BookmarkDomain merge(@MappingTarget BookmarkDomain source, BookmarkDomain.UpdateRequest r);

}
