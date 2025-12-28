package com.jinelei.bitterling.web.controller;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.stream.Collectors;

import com.jinelei.bitterling.web.enums.BookmarkType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.service.BookmarkService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "书签管理", description = "书签相关接口")
public class BookmarkController extends BaseController {
    protected final BookmarkService service;

    public BookmarkController(BookmarkService service) {
        this.service = service;
    }

    @PostMapping("import")
    @Operation(summary = "导入书签", description = "导入书签")
    public GenericResult<String> importFromFile(@RequestParam("file") MultipartFile file) throws IOException {
        this.service.importFromFile(file);
        return GenericResult.success("success");
    }

    @PostMapping("create")
    @Operation(summary = "新增书签", description = "新增书签")
    public GenericResult<String> create(@RequestBody @JsonView(BookmarkDomain.Views.Create.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return GenericResult.success("success");
    }

    @PostMapping("update")
    @Operation(summary = "更新书签", description = "根据id更新书签")
    public GenericResult<String> update(@RequestBody @JsonView(BookmarkDomain.Views.Update.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return GenericResult.success("success");
    }

    @PostMapping("delete")
    @Operation(summary = "删除书签", description = "根据id删除书签")
    public GenericResult<String> delete(@RequestBody @JsonView(BookmarkDomain.Views.Delete.class) BookmarkDomain req) {
        log.info("req: {}", req);
        Long id = Optional.ofNullable(req).map(i -> i.getId())
                .orElseThrow(() -> new InvalidParameterException("需要删除的ID不能为空"));
        this.service.deleteById(id);
        return GenericResult.success("success");
    }

    @PostMapping("list")
    @Operation(summary = "查询书签列表", description = "查询书签列表")
    public GenericResult<List<BookmarkDomain>> list(@RequestBody @JsonView(BookmarkDomain.Views.Query.class) BookmarkDomain req) {
        TimeTracker.getInstance().mark("查询书签列表");
        Iterable<BookmarkDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        return GenericResult.success(list);
    }

}
