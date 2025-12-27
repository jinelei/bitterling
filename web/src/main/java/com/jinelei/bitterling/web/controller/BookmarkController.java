package com.jinelei.bitterling.web.controller;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.response.GenericResult;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "书签管理", description = "书签相关接口")
public class BookmarkController extends BaseController<BookmarkDomain, Long> {

    public BookmarkController(BaseService<BookmarkDomain, Long> service) {
        super(service);
    }

    @PostMapping("create")
    @Operation(summary = "新增书签", description = "新增书签")
    public GenericResult<String> create(@RequestBody @JsonView(BookmarkDomain.Views.Create.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return GenericResult.of("success");
    }

    @PostMapping("update")
    @Operation(summary = "更新书签", description = "根据id更新书签")
    public GenericResult<String> update(@RequestBody @JsonView(BookmarkDomain.Views.Update.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return GenericResult.of("success");
    }

    @PostMapping("delete")
    @Operation(summary = "删除书签", description = "根据id删除书签")
    public GenericResult<String> delete(@RequestBody @JsonView(BookmarkDomain.Views.Delete.class) BookmarkDomain req) {
        log.info("req: {}", req);
        Long id = Optional.ofNullable(req).map(i -> i.getId())
                .orElseThrow(() -> new InvalidParameterException("需要删除的ID不能为空"));
        this.service.deleteById(id);
        return GenericResult.of("success");
    }

    @PostMapping("list")
    @Operation(summary = "查询书签列表", description = "查询书签列表")
    public GenericResult<String> list(@RequestBody @JsonView(BookmarkDomain.Views.Query.class) BookmarkDomain req) {
        TimeTracker.getInstance().mark("查询书签列表");
        log.info("req: {}", req);
        this.service.findAll();
        if (Math.random() > 0.2) {
            throw new NullPointerException("测试空指针");
        }
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        return GenericResult.of("success");
    }

}
