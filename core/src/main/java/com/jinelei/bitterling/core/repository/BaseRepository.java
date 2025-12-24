package com.jinelei.bitterling.core.repository;

import org.springframework.data.repository.CrudRepository;

import com.jinelei.bitterling.core.domain.BaseDomain;

public interface BaseRepository<ENT extends BaseDomain<ID>, ID> extends CrudRepository<ENT,ID> {
}
