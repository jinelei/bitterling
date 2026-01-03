package com.jinelei.bitterling.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.web.service.BookmarkService;
import com.jinelei.bitterling.web.service.UserService;

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
    private final UserService userService;

    public IndexController(BookmarkService bookmarkService, UserService userService) {
        this.bookmarkService = bookmarkService;
        this.userService = userService;
    }

    /**
     * 导航页面入口
     */
    @GetMapping(value = { "/", "/index" })
    public ModelAndView index() {
        return new ModelAndView("index", bookmarkService.indexRenderProperties());
    }

    @GetMapping("/login")
    public ModelAndView login(
            @RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "expired", required = false) String expired,
            @RequestParam(value = "username", required = false) String username,
            ModelAndView model) {
        model.setViewName("login");
        if (username != null) {
            model.addObject("username", username);
            log.error("用户登录成功: {}", username);
        }
        if (error != null) {
            model.addObject("errorMsg", "用户名或密码错误，请重新输入！");
            log.error("用户登录失败: 用户名或密码错误，请重新输入！");
        }
        if (expired != null) {
            model.addObject("errorMsg", "用户登录已过期，请重新登录！");
            log.error("用户登录失败: 用户登录已过期，请重新登录！");
        }
        return model;
    }

}
