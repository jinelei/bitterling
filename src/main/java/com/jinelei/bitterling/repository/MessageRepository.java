package com.jinelei.bitterling.repository;

import org.springframework.stereotype.Repository;

import com.jinelei.bitterling.domain.MessageDomain;

@Repository
public interface MessageRepository extends BaseRepository<MessageDomain, Long> {

}
