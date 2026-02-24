package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.BookmarkResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@SuppressWarnings("unused")
@Schema(description = "书签单个返回结果")
public class BookmarkSingleResult extends GenericResult<BookmarkResponse> {
    public BookmarkSingleResult() {
    }

    public BookmarkSingleResult(BookmarkResponse data) {
        super(data);
    }

    public BookmarkSingleResult(Integer code, String message, BookmarkResponse data) {
        super(code, message, data);
    }
}