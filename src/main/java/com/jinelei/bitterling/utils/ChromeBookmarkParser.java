package com.jinelei.bitterling.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;

/**
 * Chrome 书签解析工具（封装为 BookmarkDomain 树形结构）
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChromeBookmarkParser {
    public static final String HREF = "HREF";
    public static final String ICON = "ICON";
    public static final String ADD_DATE = "ADD_DATE";
    public static final String LAST_MODIFIED = "LAST_MODIFIED";
    public static final String DT = "DT";
    public static final String H_3 = "H3";
    public static final String A = "A";
    public static final String DL = DL1;
    public static final String DL1 = "DL";
    private static long currentId = 1L;
    private final ObjectMapper objectMapper;
    private static final Function<String, String> CLEAN_TEXT = text -> Optional.ofNullable(text)
            .map(t -> t.replaceAll("[\\u200B\\u200C\\u200D\\uFEFF\\s]+", " "))
            .map(String::trim)
            .orElse("");

    private static final Function<String, LocalDateTime> PARSE_LOCAL_DATE_TIME = s -> Optional.ofNullable(s)
            .filter(StringUtils::hasLength)
            .map(Long::parseLong)
            .map(t -> t * 1000)
            .map(Date::new)
            .map(Date::toInstant)
            .map(i -> LocalDateTime.ofInstant(i, ZoneId.systemDefault()))
            .orElse(null);

    /**
     * 解析 Chrome 书签 HTML
     *
     * @param inputStream 书签流
     * @param baseUri     baseUri
     * @return 根节点（所有书签/文件夹的父节点）
     * @throws IOException 文件读取异常
     */
    public BookmarkDomain parse(InputStream inputStream, String baseUri) throws IOException {
        return parse(Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri));
    }

    /**
     * 解析 Chrome 书签 HTML
     *
     * @param filePath 书签文件路径
     * @return 根节点（所有书签/文件夹的父节点）
     * @throws IOException 文件读取异常
     */
    public BookmarkDomain parse(String filePath) throws IOException {
        return parse(Jsoup.parse(new File(filePath), StandardCharsets.UTF_8.name()));
    }

    /**
     * 解析 Chrome 书签 HTML
     *
     * @param file 书签文件
     * @return 根节点（所有书签/文件夹的父节点）
     * @throws IOException 文件读取异常
     */
    public BookmarkDomain parse(File file) throws IOException {
        return parse(Jsoup.parse(file, StandardCharsets.UTF_8.name()));
    }

    /**
     * 解析 Chrome 书签 HTML
     *
     * @param doc 书签文件
     * @return 根节点（所有书签/文件夹的父节点）
     */
    public BookmarkDomain parse(Document doc) {
        final BookmarkDomain root = new BookmarkDomain();
        root.setId(currentId++);
        root.setName("书签根目录");
        root.setType(BookmarkType.FOLDER);
        root.setChildren(new ArrayList<>());
        final Elements rootDl = doc.select(DL);


        // 找到所有顶级 DL 节点（Chrome 书签的核心容器）
        Elements allTopDl = doc.select(DL);
        for (Element dl : allTopDl) {
            // 递归解析每个 DL 节点，挂载到根目录
            parseDlRecursive(dl, root);
        }
//        Optional.of(rootDl)
//                .filter(n -> Objects.nonNull(n.first()))
//                .map(Elements::first)
//                .ifPresent(n -> parseDlNode(n, root));
        return root;
    }

    /**
     * 递归解析 DL 节点（核心修复：支持任意层级嵌套）
     *
     * @param dlElement    待解析的 DL 节点
     * @param parentFolder 当前 DL 所属的父文件夹
     */
    private void parseDlRecursive(Element dlElement, BookmarkDomain parentFolder) {
        if (dlElement == null) return;

        // 遍历 DL 下的所有直接子节点
        for (Node childNode : dlElement.childNodes()) {
            // 只处理 DT 标签（书签/文件夹的容器）
            if (!(childNode instanceof Element) || !DT.equalsIgnoreCase(((Element) childNode).tagName())) {
                continue;
            }

            Element dtElement = (Element) childNode;
            // 1. 解析文件夹（DT 下的 H3 标签）
            Elements h3List = dtElement.select(H_3);
            if (!h3List.isEmpty()) {
                parseFolder(dtElement, h3List.first(), parentFolder);
            }

            // 2. 解析书签（DT 下的 A 标签）
            Elements aList = dtElement.select(A);
            if (!aList.isEmpty()) {
                parseBookmark(aList.first(), parentFolder);
            }

            // 3. 处理 DT 内部嵌套的 DL（兼容特殊嵌套结构）
            Elements innerDl = dtElement.select(DL);
            for (Element dl : innerDl) {
                parseDlRecursive(dl, parentFolder);
            }
        }

        // 处理当前 DL 的兄弟 DL 节点（修复跨节点嵌套问题）
        Node nextSibling = dlElement.nextSibling();
        while (nextSibling != null) {
            if (nextSibling instanceof Element && DL.equals(((Element) nextSibling).tagName())) {
                parseDlRecursive((Element) nextSibling, parentFolder);
            }
            nextSibling = nextSibling.nextSibling();
        }
    }

    /**
     * 递归解析DL节点（文件夹内容容器）
     *
     * @param dlElement DL标签元素
     * @param parent    父文件夹
     */
    private void parseDlNode(Element dlElement, BookmarkDomain parent) {
        for (Node node : dlElement.childNodes()) {
            if (node instanceof Element dtElement && DT.equalsIgnoreCase(dtElement.tagName())) {
                Elements h3Elements = dtElement.select(H_3);
                Elements aElements = dtElement.select(A);
                if (!h3Elements.isEmpty()) {
                    parseFolder(h3Elements.first(), dtElement, parent);
                } else if (!aElements.isEmpty()) {
                    parseBookmark(aElements.first(), parent);
                }
            }
        }
    }

    /**
     * 解析文件夹节点（H3 + 后续的DL）
     *
     * @param h3Element    H3标签（文件夹名称/属性）
     * @param dtElement    父DT标签
     * @param parentFolder 父文件夹
     */
    private void parseFolder(Element h3Element, Element dtElement, BookmarkDomain parentFolder) {
        final BookmarkDomain folder = new BookmarkDomain();
        folder.setType(BookmarkType.FOLDER);
        if (Objects.isNull(h3Element) || Objects.isNull(dtElement) || Objects.isNull(parentFolder)) {
            return;
        }
        Optional.of(h3Element.text()).map(CLEAN_TEXT).ifPresent(folder::setName);
        Optional.of(h3Element.attr(ADD_DATE)).map(PARSE_LOCAL_DATE_TIME).ifPresent(folder::setCreateTime);
        Optional.of(h3Element.attr(LAST_MODIFIED)).map(PARSE_LOCAL_DATE_TIME).ifPresent(folder::setUpdateTime);
        List<BookmarkDomain> children = Optional.ofNullable(parentFolder.getChildren()).orElse(new ArrayList<>());
        children.add(folder);
        parentFolder.setChildren(children);
        // 查找文件夹对应的 DL（关键修复：遍历所有兄弟节点找 DL）
        Node sibling = dtElement.nextSibling();
        while (sibling != null) {
            if (sibling instanceof Element && DL.equalsIgnoreCase(((Element) sibling).tagName())) {
                // 递归解析文件夹内的 DL
                parseDlRecursive((Element) sibling, folder);
                break;
            }
            sibling = sibling.nextSibling();
        }
        // 兜底：检查 DT 内部是否有 DL（兼容嵌套写法）
        Elements innerDl = dtElement.select(DL);
        if (innerDl.size() > 0) {
            parseDlRecursive(innerDl.first(), folder);
        }
    }

    /**
     * 解析书签节点（A标签）
     *
     * @param aElement     A标签元素
     * @param parentFolder 父文件夹
     */
    private void parseBookmark(Element aElement, BookmarkDomain parentFolder) {
        final BookmarkDomain bookmark = new BookmarkDomain();
        bookmark.setType(BookmarkType.FOLDER);
        if (Objects.isNull(aElement) || Objects.isNull(parentFolder)) {
            return;
        }
        Optional.of(aElement.text()).map(CLEAN_TEXT).ifPresent(bookmark::setName);
        Optional.of(aElement.attr(HREF)).map(CLEAN_TEXT).ifPresent(bookmark::setUrl);
        Optional.of(aElement.attr(ICON)).map(CLEAN_TEXT).ifPresent(bookmark::setIcon);
        Optional.of(aElement.attr(ADD_DATE)).map(PARSE_LOCAL_DATE_TIME).ifPresent(bookmark::setCreateTime);
        Optional.of(aElement.attr(LAST_MODIFIED)).map(PARSE_LOCAL_DATE_TIME).ifPresent(bookmark::setUpdateTime);
        List<BookmarkDomain> children = Optional.ofNullable(parentFolder.getChildren()).orElse(new ArrayList<>());
        children.add(bookmark);
        parentFolder.setChildren(children);
    }

}