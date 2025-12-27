package com.jinelei.bitterling.web.helper;

import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.enums.BookmarkType;

import org.apache.juli.logging.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * 浏览器书签解析工具类（适配 BookmarkDomain 实体）
 * 解析 NETSCAPE-Bookmark-file-1 格式的书签 HTML，转换为 BookmarkDomain 列表
 */
public class BookmarkParser {
    private static final Logger log = LoggerFactory.getLogger(BookmarkParser.class);
    // 时区（适配项目默认时区）
    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    // 用于生成排序值的计数器
    private static int orderCounter = 0;
    private static Path tempFile;

    /**
     * 从文件解析书签，转换为 BookmarkDomain 列表
     * 
     * @param filePath 书签 HTML 文件路径
     * @return BookmarkDomain 列表（包含文件夹和书签项，通过 type 区分）
     * @throws IOException 文件读取异常
     */
    public static List<BookmarkDomain> parseFromFile(String filePath) throws IOException {
        return parseFromFile(new File(filePath));
    }

    /**
     * 从文件解析书签
     * 
     * @param file 书签 HTML 文件
     * @return BookmarkDomain 列表
     * @throws IOException 文件读取异常
     */
    public static List<BookmarkDomain> parseFromFile(File file) throws IOException {
        Document doc = Jsoup.parse(file, "UTF-8");
        return parseDocument(doc);
    }

    /**
     * 从文件解析书签
     * 
     * @param file 书签 HTML 文件
     * @return BookmarkDomain 列表
     * @throws IOException 文件读取异常
     */
    public static List<BookmarkDomain> parseFromFile(MultipartFile file) throws IOException {
        log.debug("解析文件: {}", file);
        Path tempFile = Files.createTempFile("bookmark_", "_import");
        file.transferTo(tempFile);
        String content = Files.readString(tempFile, StandardCharsets.UTF_8);
        Document doc = Jsoup.parse(content, "UTF-8");
        return parseDocument(doc);
    }

    /**
     * 从 HTML 字符串解析书签
     * 
     * @param html 书签 HTML 字符串
     * @return BookmarkDomain 列表
     */
    public static List<BookmarkDomain> parseFromString(String html) {
        Document doc = Jsoup.parse(html);
        return parseDocument(doc);
    }

    /**
     * 核心解析逻辑：将 HTML 转换为 BookmarkDomain 列表
     */
    private static List<BookmarkDomain> parseDocument(Document doc) {
        List<BookmarkDomain> bookmarkList = new ArrayList<>();
        orderCounter = 0; // 重置排序计数器

        // 找到书签的核心 DL 标签
        Elements dlElements = doc.select("dl");
        if (!dlElements.isEmpty()) {
            // 解析根层级书签
            parseDlElement(dlElements.first(), bookmarkList, 1);
        }

        return bookmarkList;
    }

    /**
     * 递归解析 DL 标签，生成 BookmarkDomain 对象
     * 
     * @param dlElement    DL 元素
     * @param bookmarkList 存储解析结果的列表
     * @param level        当前层级（用于排序和展示）
     */
    private static void parseDlElement(Element dlElement, List<BookmarkDomain> bookmarkList, int level) {
        Elements dtElements = dlElement.select("> dt");
        for (Element dt : dtElements) {
            // 1. 解析文件夹（H3 标签）
            Element h3 = dt.selectFirst("h3");
            if (h3 != null) {
                BookmarkDomain folderDomain = parseFolderToDomain(h3, level);
                bookmarkList.add(folderDomain);

                // 递归解析文件夹下的子节点
                Element childDl = dt.selectFirst("> dl");
                if (childDl != null) {
                    parseDlElement(childDl, bookmarkList, level + 1);
                }
            }

            // 2. 解析书签项（A 标签）
            Element a = dt.selectFirst("a");
            if (a != null) {
                BookmarkDomain itemDomain = parseItemToDomain(a, level);
                bookmarkList.add(itemDomain);
            }
        }
    }

    /**
     * 将文件夹（H3 标签）转换为 BookmarkDomain
     */
    private static BookmarkDomain parseFolderToDomain(Element h3, int level) {
        BookmarkDomain domain = new BookmarkDomain();
        // 基础属性
        domain.setName(h3.text().trim());
        domain.setType(BookmarkType.FOLDER);
        domain.setUrl(""); // 文件夹无URL
        domain.setOrderNumber(++orderCounter); // 递增排序值

        // 时间转换：Unix 时间戳（秒）→ LocalDateTime
        String addDateStr = h3.attr("add_date");
        LocalDateTime createTime = parseUnixTimeToLocalDateTime(addDateStr);
        domain.setCreateTime(createTime);

        // 最后修改时间
        String lastModifiedStr = h3.attr("last_modified");
        LocalDateTime updateTime = StringUtils.hasText(lastModifiedStr)
                ? parseUnixTimeToLocalDateTime(lastModifiedStr)
                : createTime;
        domain.setUpdateTime(updateTime);

        // 图标：文件夹默认无图标
        domain.setIcon("");

        log.debug("解析文件夹：{}，排序值：{}", domain.getName(), domain.getOrderNumber());
        return domain;
    }

    /**
     * 将书签项（A 标签）转换为 BookmarkDomain
     */
    private static BookmarkDomain parseItemToDomain(Element a, int level) {
        BookmarkDomain domain = new BookmarkDomain();
        // 基础属性
        domain.setName(a.text().trim());
        domain.setType(BookmarkType.ITEM);
        domain.setUrl(a.attr("href").trim());
        domain.setOrderNumber(++orderCounter); // 递增排序值

        // 时间转换
        String addDateStr = a.attr("add_date");
        LocalDateTime createTime = parseUnixTimeToLocalDateTime(addDateStr);
        domain.setCreateTime(createTime);
        domain.setUpdateTime(createTime); // 书签项默认更新时间=创建时间

        // 图标：base64 编码（可选）
        String icon = a.attr("icon");
        domain.setIcon(StringUtils.hasText(icon) ? icon : "");

        log.debug("解析书签项：{}，URL：{}，排序值：{}", domain.getName(), domain.getUrl(), domain.getOrderNumber());
        return domain;
    }

    /**
     * 将 Unix 时间戳（秒级）转换为 LocalDateTime
     */
    private static LocalDateTime parseUnixTimeToLocalDateTime(String timeStr) {
        if (!StringUtils.hasText(timeStr)) {
            return LocalDateTime.now(); // 无时间戳则使用当前时间
        }

        try {
            long timestamp = Long.parseLong(timeStr);
            return LocalDateTime.ofInstant(
                    Instant.ofEpochSecond(timestamp),
                    DEFAULT_ZONE_ID);
        } catch (NumberFormatException e) {
            log.warn("解析书签时间戳失败：{}，使用当前时间", timeStr, e);
            return LocalDateTime.now();
        }
    }

}
