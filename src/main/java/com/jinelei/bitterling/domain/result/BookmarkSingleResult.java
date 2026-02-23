package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.BookmarkResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "书签单个返回结果")
public class BookmarkSingleResult extends GenericResult<BookmarkResponse> {
}