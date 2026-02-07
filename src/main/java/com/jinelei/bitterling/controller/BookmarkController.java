package com.jinelei.bitterling.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.utils.TimeTracker;
import com.jinelei.bitterling.utils.TreeUtils;
import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "书签管理", description = "书签相关接口")
public class BookmarkController extends BaseController {
    private final BookmarkService service;

    public BookmarkController(BookmarkService service) {
        this.service = service;
    }

    @PostMapping("create")
    @Operation(operationId = "bookmarkCreate", summary = "新增书签", description = "新增书签")
    public GenericResult<String> create(@RequestBody @Valid BookmarkDomain.CreateRequest req) {
        this.service.save(req);
        return GenericResult.success("success");
    }

    @PostMapping("update")
    @Operation(operationId = "bookmarkUpdate", summary = "更新书签", description = "根据id更新书签")
    public GenericResult<String> update(@RequestBody @Valid BookmarkDomain.UpdateRequest req) {
        this.service.update(req);
        return GenericResult.success("success");
    }

    @PostMapping("delete")
    @Operation(operationId = "bookmarkDelete", summary = "删除书签", description = "根据id删除书签")
    public GenericResult<String> delete(@RequestBody @Valid BookmarkDomain.DeleteRequest req) {
        this.service.deleteById(req.id());
        return GenericResult.success("success");
    }

    @PostMapping("get")
    @Operation(operationId = "bookmarkGet", summary = "查询书签列表", description = "查询书签列表")
    public GenericResult<BookmarkDomain> get(@RequestBody @Valid BookmarkDomain.GetRequest req) {
        BookmarkDomain bookmark = this.service.findById(req.id()).orElseThrow(() -> new BusinessException("未找到书签"));
        return GenericResult.success(bookmark);
    }

    @PostMapping("list")
    @Operation(operationId = "bookmarkList", summary = "查询书签列表", description = "查询书签列表")
    public GenericResult<List<BookmarkDomain>> list(@RequestBody @Valid BookmarkDomain.ListRequest req) {
        Iterable<BookmarkDomain> all = this.service.findList(req);
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        return GenericResult.success(list);
    }

    @PostMapping("tree")
    @Operation(operationId = "bookmarkTree", summary = "查询书签树", description = "查询书签树")
    public GenericResult<List<BookmarkDomain>> tree() {
        List<BookmarkDomain> tree = this.service.tree();
        return GenericResult.success(tree);
    }

    @PostMapping("myFavoriteBookmarks")
    @Operation(operationId = "myFavoriteBookmarks", summary = "我收藏的书签", description = "我收藏的书签")
    public GenericResult<List<BookmarkDomain>> myFavoriteBookmarks() {
        Iterable<BookmarkDomain> all = this.service.myFavoriteBookmarks();
        List<BookmarkDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        return GenericResult.success(list);
    }

    @PostMapping("sort")
    @Operation(operationId = "bookmarkSort", summary = "书签排序", description = "书签排序")
    public GenericResult<String> bookmarkSort(@RequestBody List<Long> ids) {
        this.service.sort(ids);
        return GenericResult.success("排序成功");
    }


}
