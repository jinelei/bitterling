package com.jinelei.bitterling.web.repository;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.web.domain.MemoDomain;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends BaseRepository<MemoDomain, Long> {

}
