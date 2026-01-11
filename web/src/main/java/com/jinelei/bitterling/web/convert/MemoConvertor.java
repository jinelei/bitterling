package com.jinelei.bitterling.web.convert;

import com.jinelei.bitterling.web.domain.MemoDomain;
import org.mapstruct.Mapper;

/**
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 * @version v1.0.0
 **/
@Mapper(componentModel = "spring")
public interface MemoConvertor {

    MemoDomain fromCreate(MemoDomain.CreateRequest r);

}
