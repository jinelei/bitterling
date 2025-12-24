package com.jinelei.bitterling.web.repository;

import org.springframework.stereotype.Repository;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.web.domain.BookmarkDomain;

@Repository
public interface BookmarkRepository extends BaseRepository<BookmarkDomain, Long> {

}
