package com.jinelei.bitterling.domain.base;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * 基础领域对象
 */
@MappedSuperclass
public abstract class BaseDomain<ID> {
    /**
     * 获取ID
     *
     * @return ID
     */
    @Id
    public abstract ID getId();

    /**
     * 设置ID
     *
     * @param id ID
     */
    public abstract void setId(ID id);
}
