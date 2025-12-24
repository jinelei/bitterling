package com.jinelei.bitterling.core.domain;

public abstract class BaseDomain<ID> {
    /**
     * 获取ID
     * 
     * @return ID
     */
    public abstract ID getId();

    /**
     * 设置ID
     * 
     * @param id ID
     */
    public abstract void setId(ID id);
}
