package com.jinelei.bitterling.web.controller;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.domain.result.GenericResult;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.web.config.SpringBeanUtils;
import com.jinelei.bitterling.web.convert.MemoConvertor;
import com.jinelei.bitterling.web.domain.MemoDomain;
import com.jinelei.bitterling.web.domain.dto.MemoPageRequest;
import com.jinelei.bitterling.web.service.MemoService;
import com.jinelei.bitterling.web.service.IndexService;
import com.jinelei.bitterling.web.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.InvalidParameterException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/memo")
@Tag(name = "备忘管理", description = "备忘相关接口")
public class MemoController extends BaseController {
    private final MemoService service;
    private final IndexService indexService;
    private final MessageService messageService;

    public MemoController(MemoService service, IndexService indexService, MessageService messageService) {
        this.service = service;
        this.indexService = indexService;
        this.messageService = messageService;
    }

    @GetMapping(value = {"", "/", "/index"})
    public ModelAndView indexPage(MemoPageRequest request, Principal principal) {
        log.info("index request: {}", request);
        ModelAndView modelAndView = new ModelAndView("memo/index");
        modelAndView.addAllObjects(service.renderIndex(request));
        modelAndView.addObject("greeting", indexService.getGreeting());
        modelAndView.addObject("unreadMessage", messageService.unreadMessages());
        modelAndView.addObject("username", Optional.ofNullable(principal).map(Principal::getName).orElse("匿名用户"));
        log.info("modelAndView: {}", modelAndView);
        return modelAndView;
    }

    @GetMapping(value = {"/{id}"})
    public ModelAndView detailPage(@PathVariable("id") Long id, MemoPageRequest request, Principal principal) {
        log.info("detail request: {}, {}", id, request);
        ModelAndView modelAndView = new ModelAndView("memo/detail");
        Optional.ofNullable(id).orElseThrow(() -> new BusinessException("id不能为空"));
        Optional.ofNullable(request).ifPresent(r -> r.setId(id));
        modelAndView.addAllObjects(service.renderDetail(request));
        modelAndView.addObject("greeting", indexService.getGreeting());
        modelAndView.addObject("unreadMessage", messageService.unreadMessages());
        modelAndView.addObject("username", Optional.ofNullable(principal).map(Principal::getName).orElse("匿名用户"));
        log.info("modelAndView: {}", modelAndView);
        return modelAndView;
    }

    @GetMapping(value = {"/edit/{id}"})
    public ModelAndView editPage(@PathVariable("id") Long id, MemoPageRequest request, Principal principal) {
        log.info("edit request: {}, {}", id, request);
        ModelAndView modelAndView = new ModelAndView("memo/edit");
        Optional.ofNullable(id).orElseThrow(() -> new BusinessException("id不能为空"));
        Optional.ofNullable(request).ifPresent(r -> r.setId(id));
        modelAndView.addAllObjects(service.renderDetail(request));
        modelAndView.addObject("greeting", indexService.getGreeting());
        modelAndView.addObject("unreadMessage", messageService.unreadMessages());
        modelAndView.addObject("username", Optional.ofNullable(principal).map(Principal::getName).orElse("匿名用户"));
        log.info("modelAndView: {}", modelAndView);
        return modelAndView;
    }

    @GetMapping(value = {"/create"})
    public ModelAndView createPage(Principal principal) {
        ModelAndView modelAndView = new ModelAndView("memo/create");
        modelAndView.addAllObjects(service.renderCreate());
        modelAndView.addObject("greeting", indexService.getGreeting());
        modelAndView.addObject("unreadMessage", messageService.unreadMessages());
        modelAndView.addObject("username", Optional.ofNullable(principal).map(Principal::getName).orElse("匿名用户"));
        log.info("modelAndView: {}", modelAndView);
        return modelAndView;
    }

    @PostMapping("create")
    @Operation(summary = "新增备忘", description = "新增备忘")
    public RedirectView create(@Valid MemoDomain.CreateRequest req) {
        log.info("create: {}", req);
        this.service.create(req);
        return new RedirectView("/memo");
    }

    @PostMapping("update")
    @Operation(summary = "更新备忘", description = "根据id更新备忘")
    public RedirectView update(@Valid MemoDomain.UpdateRequest req) {
        log.info("update: {}", req);
        this.service.update(req);
        return new RedirectView("/memo");
    }

    @PostMapping("delete")
    @Operation(summary = "删除备忘", description = "根据id删除备忘")
    public GenericResult<String> delete(@RequestBody MemoDomain.DeleteRequest req) {
        log.info("delete: {}", req);
        Long id = Optional.ofNullable(req).map(MemoDomain.DeleteRequest::id)
                .orElseThrow(() -> new InvalidParameterException("需要删除的ID不能为空"));
        this.service.deleteById(id);
        return GenericResult.success("success");
    }

    @PostMapping("list")
    @Operation(summary = "查询备忘列表", description = "查询备忘列表")
    public GenericResult<List<MemoDomain>> list(@RequestBody MemoDomain.ListQueryRequest req) {
        log.info("list: {}", req);
        TimeTracker.getInstance().mark("查询备忘列表");
        Iterable<MemoDomain> all = this.service.findAll();
        TimeTracker.getInstance().mark("查询备忘列表").printTotalTime("查询备忘列表");
        List<MemoDomain> list = new ArrayList<>();
        all.forEach(list::add);
        return GenericResult.success(list);
    }

    @ResponseBody
    @GetMapping("render/{id}")
    @Operation(summary = "查询备忘渲染", description = "查询备忘渲染")
    public GenericResult<MemoDomain> render(@PathVariable("id") Long id) {
        log.info("render: {}", id);
        TimeTracker.getInstance().mark("查询备忘列表");
        MemoDomain byId = this.service.findById(id).orElseThrow(() -> new BusinessException("未找到备忘"));
        TimeTracker.getInstance().mark("查询备忘列表").printTotalTime("查询备忘列表");
        return GenericResult.success(byId);
    }

}
