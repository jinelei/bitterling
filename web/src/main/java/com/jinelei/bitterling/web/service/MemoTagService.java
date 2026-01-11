package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class MemoTagService extends BaseService<MemoTagDomain, Long> {

    public MemoTagService(BaseRepository<MemoTagDomain, Long> repository, Validator validator) {
        super(repository, validator);
    }

}
