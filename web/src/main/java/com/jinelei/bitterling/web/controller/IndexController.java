package com.jinelei.bitterling.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.web.service.BookmarkService;

/**
 * 首页控制器
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2025-12-28
 */
@Controller
public class IndexController extends BaseController {
    private final BookmarkService bookmarkService;

    public IndexController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }

    /**
     * 导航页面入口
     */
    @GetMapping(value = { "/", "/index" })
    public ModelAndView index() {
        return new ModelAndView("index", bookmarkService.indexRenderProperties());
    }

}
