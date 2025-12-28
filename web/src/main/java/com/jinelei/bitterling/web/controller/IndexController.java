package com.jinelei.bitterling.web.controller;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;
import com.jinelei.bitterling.web.service.BookmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 首页控制器
 *
 * @author zhenlei
 * @version 1.0.0
 * @date 2025-12-28
 */
@Controller
public class IndexController extends BaseController {
    private final BookmarkService service;

    public IndexController(BookmarkService service) {
        this.service = service;
    }

    /**
     * 导航页面入口
     */
    @GetMapping(value = {"/", "/index"})
    public String nav(Model model) {
        // 模拟书签数据（包含文件夹+书签项）
        Iterable<BookmarkDomain> all = service.findAll();
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        List<BookmarkDomain> bookmarks = new ArrayList<>();
        all.forEach(bookmarks::add);

        // 按类型分组，便于前端分类展示
        Map<BookmarkType, List<BookmarkDomain>> groupByType = bookmarks.stream()
                .sorted(Comparator.comparingInt(BookmarkDomain::getOrderNumber)) // 按排序值升序
                .collect(Collectors.groupingBy(BookmarkDomain::getType));

        model.addAttribute("bookmarkGroups", groupByType);
        // 传递枚举描述，便于前端展示
        model.addAttribute("folderDesc", "文件夹");
        model.addAttribute("itemDesc", "书签项");
        return "nav";
    }

}
