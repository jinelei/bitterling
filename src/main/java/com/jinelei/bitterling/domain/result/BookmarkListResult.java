package com.jinelei.bitterling.domain.result;

import com.jinelei.bitterling.domain.response.BookmarkResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "书签列表返回结果")
public class BookmarkListResult extends CollectionResult<List<BookmarkResponse>> {
    public BookmarkListResult() {
    }

    public BookmarkListResult(List<BookmarkResponse> data) {
        super(data);
    }

    public BookmarkListResult(List<BookmarkResponse> data, Long total) {
        super(data, total);
    }

    public BookmarkListResult(Integer code, String message, List<BookmarkResponse> data) {
        super(code, message, data);
    }

    public BookmarkListResult(Integer code, String message, List<BookmarkResponse> data, Long total) {
        super(code, message, data, total);
    }
}