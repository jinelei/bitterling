package com.jinelei.bitterling.controller;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import com.jinelei.bitterling.exception.BusinessException;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.jinelei.bitterling.domain.result.GenericResult;
import com.jinelei.bitterling.utils.TimeTracker;
import com.jinelei.bitterling.utils.TreeUtils;
import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.service.BookmarkService;
import com.jinelei.bitterling.service.IndexService;
import com.jinelei.bitterling.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "书签管理", description = "书签相关接口")
public class BookmarkController extends BaseController {
    private final BookmarkService service;
    private final IndexService indexService;
    private final MessageService messageService;

    public BookmarkController(BookmarkService service, IndexService indexService, MessageService messageService) {
        this.service = service;
        this.indexService = indexService;
        this.messageService = messageService;
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
    public GenericResult<List<BookmarkDomain>> list(@RequestBody BookmarkDomain req) {
        log.info("list: {}", req);
        TimeTracker.getInstance().mark("查询书签列表");
        Iterable<BookmarkDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        return GenericResult.success(list);
    }

    @PostMapping("tree")
    @Operation(operationId = "bookmarkTree", summary = "查询书签树", description = "查询书签树")
    public GenericResult<List<BookmarkDomain>> tree(@RequestBody BookmarkDomain req) {
        log.info("tree: {}", req);
        TimeTracker.getInstance().mark("查询书签树");
        Iterable<BookmarkDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询书签树").printTotalTime("查询书签树");
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        List<BookmarkDomain> tree = TreeUtils.convertToTree(list);
        return GenericResult.success(tree);
    }

    @PostMapping("myFavoriteBookmarks")
    @Operation(operationId = "myFavoriteBookmarks", summary = "我收藏的书签", description = "我收藏的书签")
    public GenericResult<List<BookmarkDomain>> myFavoriteBookmarks() {
        Iterable<BookmarkDomain> all = this.service.myFavoriteBookmarks();
        List<BookmarkDomain> list = StreamSupport.stream(all.spliterator(), false).toList();
        return GenericResult.success(list);
    }

}
