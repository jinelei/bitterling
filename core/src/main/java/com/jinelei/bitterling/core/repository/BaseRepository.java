package com.jinelei.bitterling.core.repository;

import org.springframework.data.repository.CrudRepository;

import com.jinelei.bitterling.core.domain.BaseDomain;

/**
 * 基础仓库
 */
public interface BaseRepository<ENT extends BaseDomain<ID>, ID> extends CrudRepository<ENT, ID> {
}
