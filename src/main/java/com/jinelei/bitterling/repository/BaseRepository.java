package com.jinelei.bitterling.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import com.jinelei.bitterling.domain.base.BaseDomain;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * 基础仓库
 */
@NoRepositoryBean
public interface BaseRepository<ENT extends BaseDomain<ID>, ID>
        extends CrudRepository<ENT, ID>, JpaSpecificationExecutor<ENT> {
}
