package com.jinelei.bitterling.web.service;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;

@Service
public class MemoTagRelateService extends BaseService<MemoTagRelateRecordDomain, MemoTagPrimaryKey> {

    public MemoTagRelateService(BaseRepository<MemoTagRelateRecordDomain, MemoTagPrimaryKey> repository, Validator validator) {
        super(repository, validator);
    }

}
