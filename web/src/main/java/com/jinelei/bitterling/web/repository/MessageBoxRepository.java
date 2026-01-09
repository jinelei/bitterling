package com.jinelei.bitterling.web.repository;

import org.springframework.stereotype.Repository;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.web.domain.MessageBoxDomain;

@Repository
public interface MessageBoxRepository extends BaseRepository<MessageBoxDomain, Long> {

}
