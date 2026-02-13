package com.jinelei.bitterling.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.domain.convert.BookmarkConvertor;
import com.jinelei.bitterling.domain.request.*;
import com.jinelei.bitterling.domain.response.BookmarkResponse;
import com.jinelei.bitterling.exception.BusinessException;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.jinelei.bitterling.domain.result.GenericResult;
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
    @ApiResponse(responseCode = "200", description = "新增成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> create(@RequestBody @Valid BookmarkCreateRequest req) {
        this.service.save(req);
        return GenericResult.success("success");
    }

    @PostMapping("update")
    @Operation(operationId = "bookmarkUpdate", summary = "更新书签", description = "根据id更新书签")
    @ApiResponse(responseCode = "200", description = "更新成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> update(@RequestBody @Valid BookmarkUpdateRequest req) {
        this.service.update(req);
        return GenericResult.success("success");
    }

    @PostMapping("delete")
    @Operation(operationId = "bookmarkDelete", summary = "删除书签", description = "根据id删除书签")
    @ApiResponse(responseCode = "200", description = "删除成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> delete(@RequestBody @Valid BookmarkDeleteRequest req) {
        this.service.deleteById(req.id());
        return GenericResult.success("success");
    }

    @PostMapping("get")
    @Operation(operationId = "bookmarkGet", summary = "查询书签列表", description = "查询书签列表")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<BookmarkResponse> get(@RequestBody @Valid BookmarkGetRequest req) {
        BookmarkDomain bookmark = this.service.findById(req.id()).orElseThrow(() -> new BusinessException("未找到书签"));
        BookmarkResponse response = bookmarkConvertor.toResponse(bookmark);
        return GenericResult.success(response);
    }

    @PostMapping("list")
    @Operation(operationId = "bookmarkList", summary = "查询书签列表", description = "查询书签列表")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<List<BookmarkResponse>> list(@RequestBody @Valid BookmarkListRequest req) {
        Iterable<BookmarkDomain> all = this.service.findList(req);
        List<BookmarkResponse> list = StreamSupport.stream(all.spliterator(), false)
                .map(bookmarkConvertor::toResponse)
                .toList();
        return GenericResult.success(list);
    }

    @PostMapping("tree")
    @Operation(operationId = "bookmarkTree", summary = "查询书签树", description = "查询书签树")
//    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<List<BookmarkResponse>> tree() {
        List<BookmarkResponse> tree = this.service.tree();
        return GenericResult.success(tree);
    }

    @PostMapping("myFavoriteBookmarks")
    @Operation(operationId = "myFavoriteBookmarks", summary = "我收藏的书签", description = "我收藏的书签")
    @ApiResponse(responseCode = "200", description = "查询成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<List<BookmarkResponse>> myFavoriteBookmarks() {
        Iterable<BookmarkDomain> all = this.service.myFavoriteBookmarks();
        List<BookmarkResponse> list = StreamSupport.stream(all.spliterator(), false)
                .map(bookmarkConvertor::toResponse)
                .toList();
        return GenericResult.success(list);
    }

    @PostMapping("sort")
    @Operation(operationId = "bookmarkSort", summary = "书签排序", description = "书签排序")
    @ApiResponse(responseCode = "200", description = "排序成功", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = GenericResult.class)))
    public GenericResult<String> bookmarkSort(@RequestBody List<Long> ids) {
        this.service.sort(ids);
        return GenericResult.success("排序成功");
    }


}
