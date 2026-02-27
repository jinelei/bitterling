package com.jinelei.bitterling.controller;

import java.util.List;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.domain.request.*;
import com.jinelei.bitterling.domain.result.*;
import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "书签管理", description = "书签相关接口")
public class BookmarkController extends BaseController {
    private final BookmarkService service;
    private final BookmarkConvertor bookmarkConvertor;

    public BookmarkController(BookmarkService service, BookmarkConvertor bookmarkConvertor) {
        this.service = service;
        this.bookmarkConvertor = bookmarkConvertor;
    }

    @PostMapping("create")
    @Operation(operationId = "bookmarkCreate", summary = "新增书签", description = "新增书签")
    public StringResult create(@RequestBody @Valid BookmarkCreateRequest req) {
        this.service.save(req);
        return ResultFactory.create(StringResult.class, "添加成功");
    }

    @PostMapping("update")
    @Operation(operationId = "bookmarkUpdate", summary = "更新书签", description = "根据id更新书签")
    public StringResult update(@RequestBody @Valid BookmarkUpdateRequest req) {
        this.service.update(req);
        return ResultFactory.create(StringResult.class, "更新成功");
    }

    @PostMapping("delete")
    @Operation(operationId = "bookmarkDelete", summary = "删除书签", description = "根据id删除书签")
    public StringResult delete(@RequestBody @Valid BookmarkDeleteRequest req) {
        this.service.deleteById(req.id());
        return ResultFactory.create(StringResult.class, "删除成功");
    }

    @PostMapping("get")
    @Operation(operationId = "bookmarkGet", summary = "查询书签列表", description = "查询书签列表")
    public BookmarkSingleResult get(@RequestBody @Valid BookmarkGetRequest req) {
        BookmarkDomain bookmark = this.service.findById(req.id()).orElseThrow(() -> new BusinessException("未找到书签"));
        BookmarkResponse response = bookmarkConvertor.toResponse(bookmark);
        return ResultFactory.create(BookmarkSingleResult.class, response);
    }

    @PostMapping("list")
    @Operation(operationId = "bookmarkList", summary = "查询书签列表", description = "查询书签列表")
    public BookmarkListResult list(@RequestBody @Valid BookmarkListRequest req) {
        Iterable<BookmarkDomain> all = this.service.findList(req);
        List<BookmarkResponse> list = StreamSupport.stream(all.spliterator(), false)
                .map(bookmarkConvertor::toResponse)
                .toList();
        return ResultFactory.create(BookmarkListResult.class, list);
    }

    @PostMapping("tree")
    @Operation(operationId = "bookmarkTree", summary = "查询书签树", description = "查询书签树")
    public BookmarkListResult tree() {
        List<BookmarkResponse> tree = this.service.tree();
        return ResultFactory.create(BookmarkListResult.class, tree);
    }

    @PostMapping("sort")
    @Operation(operationId = "bookmarkSort", summary = "书签排序", description = "书签排序")
    public StringResult bookmarkSort(@RequestBody List<Long> ids) {
        this.service.sort(ids);
        return ResultFactory.create(StringResult.class, "排序成功");
    }


}
