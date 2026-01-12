package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import com.jinelei.bitterling.web.repository.MemoTagRepository;

import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class MemoTagService extends BaseService<MemoTagRepository, MemoTagDomain, Long> {

    public MemoTagService(MemoTagRepository repository, Validator validator) {
        super(repository, validator);
    }

}
