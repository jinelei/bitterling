package com.jinelei.bitterling.web.repository;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.web.domain.MemoTagDomain;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoTagRepository extends BaseRepository<MemoTagDomain, Long> {
}
