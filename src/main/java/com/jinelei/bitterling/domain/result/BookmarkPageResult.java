package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.BookmarkResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SuppressWarnings("unused")
@Schema(description = "书签分页返回结果")
public class BookmarkPageResult extends PageableResult<List<BookmarkResponse>> {
}