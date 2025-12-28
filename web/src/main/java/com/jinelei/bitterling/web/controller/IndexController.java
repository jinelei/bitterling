package com.jinelei.bitterling.web.controller;

import com.jinelei.bitterling.core.controller.BaseController;
import com.jinelei.bitterling.core.helper.TimeTracker;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;
import com.jinelei.bitterling.web.service.BookmarkService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView("nav"); // 对应templates/nav.html
        Iterable<BookmarkDomain> all = service.findAll();
        TimeTracker.getInstance().mark("查询书签列表").printTotalTime("查询书签列表");
        final List<BookmarkDomain> bookmarks = StreamSupport.stream(all.spliterator(), false).collect(Collectors.toList());
        final Map<Long, String> categoryMap = bookmarks.parallelStream()
                .filter(i -> BookmarkType.FOLDER.equals(i.getType()))
                .collect(Collectors.toMap(BookmarkDomain::getId, BookmarkDomain::getName));
        final List<BookmarkDomain> categories = bookmarks.parallelStream()
                .filter(i -> BookmarkType.FOLDER.equals(i.getType()))
                .filter(i -> Objects.nonNull(i.getId()))
                .filter(i -> Objects.nonNull(i.getName()))
                .collect(Collectors.toList());
        Map<Long, List<BookmarkDomain>> bookmarkGroups = bookmarks.parallelStream()
                .filter(i -> BookmarkType.ITEM.equals(i.getType()))
                .collect(Collectors.groupingBy(i -> Optional.ofNullable(i.getParentId()).orElse(0L), Collectors.toList()));
        log.info("categories: {}", categories);
        log.info("bookmarkGroups: {}", bookmarkGroups);
        mav.addObject("categories", categoryMap);
        mav.addObject("bookmarkGroups", bookmarkGroups);
        return mav;
    }
}
