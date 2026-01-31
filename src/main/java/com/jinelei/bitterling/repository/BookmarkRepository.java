package com.jinelei.bitterling.repository;

import org.springframework.stereotype.Repository;

import com.jinelei.bitterling.domain.BookmarkDomain;

@Repository
public interface BookmarkRepository extends BaseRepository<BookmarkDomain, Long> {

}
