package com.jinelei.bitterling.utils;

import lombok.*;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Chrome 书签解析工具（封装为 BookmarkDomain 树形结构）
 */
@SuppressWarnings("unused")
@Slf4j
@Component
@RequiredArgsConstructor
public class ChromeBookmarkUtil {
    public static final String DL = "dl";
    public static final String GT_DL = "> dl";
    public static final String DT_GT_H3 = "dt > h3";
    public static final String GT_DT = "> dt";
    public static final String GT_H3 = "> h3";
    public static final String GT_A = "> a";
    public static final String ADD_DATE = "ADD_DATE";
    public static final String LAST_MODIFIED = "LAST_MODIFIED";
    public static final String HREF = "HREF";
    public static final String ICON = "ICON";

    public Folder parse(InputStream inputStream, String baseUri) throws IOException {
        Document doc = Jsoup.parse(inputStream, StandardCharsets.UTF_8.name(), baseUri);
        return parse(doc);
    }

    /**
     * 解析Chrome书签HTML文件
     *
     * @param filePath 书签文件路径
     * @return 根文件夹节点
     * @throws IOException 文件读取异常
     */
    public Folder parse(String filePath) throws IOException {
        Document doc = Jsoup.parse(new File(filePath), StandardCharsets.UTF_8.name());
        return parse(doc);
    }

    /**
     * 解析Chrome书签HTML文件
     *
     * @param file 书签文件
     * @return 根文件夹节点
     * @throws IOException 文件读取异常
     */
    public Folder parse(File file) throws IOException {
        Document doc = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        return parse(doc);
    }

    /**
     * 解析Chrome书签HTML文件
     *
     * @param doc 文档
     * @return 根文件夹节点
     */
    public Folder parse(Document doc) {
        // 2. 找到书签核心DL节点（Chrome书签的根DL）
        Element rootDl = findRootBookmarkDl(doc);
        if (rootDl == null) {
            throw new IllegalArgumentException("无效的Chrome书签文件：未找到核心书签节点");
        }
        // 3. 创建根文件夹
        Folder rootFolder = Folder.builder().name("书签根目录").addTime(Instant.now().toEpochMilli()).children(new ArrayList<>()).build();
        // 4. 递归解析节点
        parseDlNode(rootDl, rootFolder);
        return rootFolder;
    }

    /**
     * 找到Chrome书签的根DL节点
     */
    private Element findRootBookmarkDl(Document doc) {
        // Chrome书签结构：<DL><p><DT><H3>书签栏</H3><DL>...</DL></DT></p></DL>
        Elements dlList = doc.select(DL);
        for (Element dl : dlList) {
            // 根DL包含H3标签（书签栏），且嵌套子DL
            if (!dl.select(DT_GT_H3).isEmpty()) {
                return dl;
            }
        }
        return !dlList.isEmpty() ? dlList.getFirst() : null;
    }

    /**
     * 递归解析DL节点（核心逻辑）
     *
     * @param dlNode       DL节点
     * @param parentFolder 父文件夹
     */
    private void parseDlNode(Element dlNode, Folder parentFolder) {
        // 遍历DL下的所有DT节点（每个DT对应一个书签/文件夹）
        Elements dtNodes = dlNode.select(GT_DT);
        for (Element dt : dtNodes) {
            // 情况1：当前DT是文件夹（包含H3标签）
            Element h3 = dt.selectFirst(GT_H3);
            if (h3 != null) {
                parseFolderNode(dt, h3, parentFolder);
            }
            // 情况2：当前DT是书签项（包含A标签）
            Element aTag = dt.selectFirst(GT_A);
            if (aTag != null) {
                parseBookmark(dt, aTag, parentFolder);
            }
        }
    }

    /**
     * 解析文件夹节点
     *
     * @param dtNode       外层DT节点
     * @param h3Node       H3标签（文件夹名称）
     * @param parentFolder 父文件夹
     */
    private void parseFolderNode(Element dtNode, Element h3Node, Folder parentFolder) {
        final Folder.FolderBuilder<?, ?> builder = Folder.builder();
        Optional.ofNullable(h3Node).map(Element::text).map(String::trim).ifPresent(builder::name);
        Optional.ofNullable(h3Node).map(a -> a.attr(ADD_DATE)).map(String::trim).filter(StringUtils::hasLength).map(Long::parseLong).ifPresent(builder::addTime);
        Optional.ofNullable(h3Node).map(a -> a.attr(LAST_MODIFIED)).map(String::trim).filter(StringUtils::hasLength).map(Long::parseLong).ifPresent(builder::lastModifiedTime);
        List<Base> children = Optional.ofNullable(parentFolder.getChildren()).orElse(new ArrayList<>());
        Folder folder = builder.build();
        children.add(folder);
        parentFolder.setChildren(children);
        // 3. 找到文件夹内的子DL节点（递归解析子节点）
        Element childDl = dtNode.selectFirst(GT_DL);
        if (childDl != null) {
            parseDlNode(childDl, folder);
        }
    }

    /**
     * 解析书签项节点
     *
     * @param dtNode       外层DT节点
     * @param aTag         A标签（书签链接）
     * @param parentFolder 父文件夹
     */
    private void parseBookmark(Element dtNode, Element aTag, Folder parentFolder) {
        final Bookmark.BookmarkBuilder<?, ?> builder = Bookmark.builder();
        Optional.ofNullable(aTag).map(Element::text).map(String::trim).ifPresent(builder::name);
        Optional.ofNullable(aTag).map(a -> a.attr(HREF)).map(String::trim).ifPresent(builder::url);
        Optional.ofNullable(aTag).map(a -> a.attr(ADD_DATE)).map(String::trim).map(Long::parseLong).ifPresent(builder::addTime);
        Optional.ofNullable(aTag).map(a -> a.attr(ICON)).map(String::trim).ifPresent(builder::icon);
        Optional.ofNullable(aTag).map(a -> a.attr(LAST_MODIFIED)).map(String::trim).filter(StringUtils::hasLength).map(Long::parseLong).ifPresent(builder::lastModifiedTime);
        List<Base> children = Optional.ofNullable(parentFolder.getChildren()).orElse(new ArrayList<>());
        children.add(builder.build());
        parentFolder.setChildren(children);
    }

    /**
     * 解析时间属性（Chrome时间戳：微秒，转长整型，异常返回0）
     */
    private long parseTimeAttribute(String timeStr) {
        if (timeStr == null || timeStr.isBlank()) {
            return 0;
        }
        try {
            return Long.parseLong(timeStr.trim());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 书签节点基类（文件夹/书签项通用）
     */
    @Data
    @SuperBuilder
    public static abstract class Base {
        // 节点名称
        protected String name;
        // 添加时间（Chrome以微秒为单位的时间戳）
        protected long addTime;
        // 最后修改时间
        protected long lastModifiedTime;
        // 父节点
        protected Folder parent;
    }

    /**
     * 书签项（具体的链接）
     */
    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Bookmark extends Base {
        // 书签URL
        protected String url;
        // 书签ICON
        protected String icon;
    }

    /**
     * 书签文件夹（可嵌套子节点）
     */
    @Data
    @SuperBuilder
    @EqualsAndHashCode(callSuper = true)
    public static class Folder extends Base {
        // 子节点（文件夹/书签项）
        protected List<Base> children;
    }

}
