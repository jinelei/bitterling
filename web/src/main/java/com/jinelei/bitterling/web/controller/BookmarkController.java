package com.jinelei.bitterling.web.controller;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController extends BaseController<BookmarkDomain, Long> {

    public BookmarkController(BaseService<BookmarkDomain, Long> service) {
        super(service);
    }

    @PostMapping("create")
    public String create(@RequestBody @JsonView(BookmarkDomain.Views.Create.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return "success";
    }

    @PostMapping("update")
    public String update(@RequestBody @JsonView(BookmarkDomain.Views.Update.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.save(req);
        return "success";
    }

    @PostMapping("delete")
    public String delete(@RequestBody @JsonView(BookmarkDomain.Views.Delete.class) BookmarkDomain req) {
        log.info("req: {}", req);
        Long id = Optional.ofNullable(req).map(i -> i.getId())
                .orElseThrow(() -> new InvalidParameterException("需要删除的ID不能为空"));
        this.service.deleteById(id);
        return "success";
    }

    @PostMapping("list")
    public String list(@RequestBody @JsonView(BookmarkDomain.Views.Query.class) BookmarkDomain req) {
        log.info("req: {}", req);
        this.service.findAll();
        return "success";
    }

}
