package com.jinelei.bitterling.web.controller;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.core.helper.TreeHelper;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.service.BookmarkService;
import com.jinelei.bitterling.web.service.IndexService;
import com.jinelei.bitterling.web.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller
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

    @GetMapping(value = {"", "/", "/index"})
    public ModelAndView index(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("bookmark");
        modelAndView.addAllObjects(service.renderIndex());
        modelAndView.addObject("title", indexService.getTitle());
        modelAndView.addObject("greeting", indexService.getGreeting());
        modelAndView.addObject("unreadMessage", messageService.unreadMessages());
        modelAndView.addObject("username", Optional.ofNullable(principal).map(Principal::getName).orElse("匿名用户"));
        log.info("modelAndView: {}", modelAndView);
        return modelAndView;
    }

    @ResponseBody
    @PostMapping("create")
    @Operation(summary = "新增书签", description = "新增书签")
    public GenericResult<String> create(@RequestBody BookmarkDomain req) {
        log.info("create: {}", req);
        this.service.save(req);
        return GenericResult.success("success");
    }

    @ResponseBody
    @PostMapping("update")
    @Operation(summary = "更新书签", description = "根据id更新书签")
    public GenericResult<String> update(@RequestBody BookmarkDomain req) {
        log.info("update: {}", req);
        this.service.save(req);
        return GenericResult.success("success");
    }

    @ResponseBody
    @PostMapping("delete")
    @Operation(summary = "删除书签", description = "根据id删除书签")
    public GenericResult<String> delete(@RequestBody BookmarkDomain req) {
        log.info("delete: {}", req);
        Long id = Optional.ofNullable(req).map(BookmarkDomain::getId)
                .orElseThrow(() -> new InvalidParameterException("需要删除的ID不能为空"));
        this.service.deleteById(id);
        return GenericResult.success("success");
    }

    @ResponseBody
    @PostMapping("list")
    @Operation(summary = "查询书签列表", description = "查询书签列表")
    public GenericResult<List<BookmarkDomain>> list(@RequestBody BookmarkDomain req) {
        log.info("list: {}", req);
        TimeTracker.getInstance().mark("查询书签列表");
        Iterable<BookmarkDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        return GenericResult.success(list);
    }

    @ResponseBody
    @PostMapping("tree")
    @Operation(summary = "查询书签树", description = "查询书签树")
    public GenericResult<List<BookmarkDomain>> tree(@RequestBody BookmarkDomain req) {
        log.info("tree: {}", req);
        TimeTracker.getInstance().mark("查询书签树");
        Iterable<BookmarkDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询书签树").printTotalTime("查询书签树");
        List<BookmarkDomain> list = new ArrayList<>();
        all.forEach(list::add);
        List<BookmarkDomain> tree = TreeHelper.convertToTree(list);
        return GenericResult.success(tree);
    }

}
