package com.jinelei.bitterling.web.repository;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.web.domain.MemoTagRelateRecordDomain;
import com.jinelei.bitterling.web.domain.pk.MemoTagPrimaryKey;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoTagRelateRepository extends BaseRepository<MemoTagRelateRecordDomain, MemoTagPrimaryKey> {
}
