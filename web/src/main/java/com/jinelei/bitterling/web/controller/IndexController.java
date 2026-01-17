package com.jinelei.bitterling.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.web.service.BookmarkService;
import com.jinelei.bitterling.web.service.IndexService;

/**
 * 首页控制器
 *
 * @author jinelei
 * @version 1.0.0
 * @date 2025-12-28
 */
@Controller
public class IndexController extends BaseController {
    private final IndexService indexService;
    private final BookmarkService bookmarkService;

    public IndexController(IndexService indexService, BookmarkService bookmarkService) {
        this.indexService = indexService;
        this.bookmarkService = bookmarkService;
    }

    @GetMapping(value = { "/", "/index" })
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("navigation/index");
        modelAndView.addAllObjects(bookmarkService.renderIndex());
        modelAndView.addObject("title", indexService.getTitle());
        modelAndView.addObject("greeting", indexService.getGreeting());
        return modelAndView;
    }

    @GetMapping(value = { "/about" })
    public ModelAndView about() {
        ModelAndView modelAndView = new ModelAndView("about/index");
        modelAndView.addObject("title", indexService.getTitle());
        modelAndView.addObject("greeting", indexService.getGreeting());
        return modelAndView;
    }

}
