package com.jinelei.bitterling.core.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.service.BaseService;

public abstract class BaseController<ENT extends BaseDomain<ID>, ID> {
    protected final BaseService<ENT, ID> service;
    protected final Logger log;

    public BaseController(BaseService<ENT, ID> service) {
        this.service = service;
        this.log = LoggerFactory.getLogger(getClass());
    }
}
