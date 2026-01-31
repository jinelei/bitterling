package com.jinelei.bitterling.repository;

import com.jinelei.bitterling.domain.MemoDomain;
import org.springframework.stereotype.Repository;

@Repository
public interface MemoRepository extends BaseRepository<MemoDomain, Long> {

}
