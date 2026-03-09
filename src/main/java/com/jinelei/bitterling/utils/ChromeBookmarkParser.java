package com.jinelei.bitterling.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinelei.bitterling.domain.BookmarkDomain;
import com.jinelei.bitterling.domain.enums.BookmarkType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Chrome 书签解析工具（封装为 BookmarkDomain 树形结构）
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ChromeBookmarkParser {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long currentId = 1L;
    private final ObjectMapper objectMapper;

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
        BookmarkDomain root = new BookmarkDomain();
        root.setId(currentId++);
        root.setName("书签根目录");
        root.setType(BookmarkType.FOLDER);
        root.setChildren(new ArrayList<>());

        Deque<BookmarkDomain> folderStack = new ArrayDeque<>();
        folderStack.push(root);
        log.info("创建根节点: {}", root.getName());

        Elements dtElements = doc.select("dt");
        for (Element dt : dtElements) {
            Element folderElement = dt.selectFirst("h3");
            if (folderElement != null) {
                String folderName = folderElement.text().trim();
                BookmarkDomain folderNode = new BookmarkDomain();
                folderNode.setId(currentId++);
                folderNode.setName(folderName);
                folderNode.setType(BookmarkType.FOLDER);
                folderNode.setParentId(folderStack.peek().getId());
                folderNode.setChildren(new ArrayList<>());

                log.info("{} 追加子文件夹 {}", folderStack.peek().getName(), folderNode.getName());
                folderStack.peek().getChildren().add(folderNode);
                folderStack.push(folderNode);
                continue;
            }

            Element bookmarkElement = dt.selectFirst("a");
            if (bookmarkElement != null) {
                BookmarkDomain bookmarkNode = new BookmarkDomain();
                bookmarkNode.setId(currentId++);
                bookmarkNode.setName(bookmarkElement.text().trim());
                bookmarkNode.setType(BookmarkType.BOOKMARK);
                bookmarkNode.setUrl(bookmarkElement.attr("href").trim());
                bookmarkNode.setParentId(folderStack.peek().getId());
                bookmarkNode.setChildren(new ArrayList<>());

                String addDateStr = bookmarkElement.attr("add_date");
                if (!addDateStr.isEmpty()) {
                    try {
                        long timestamp = Long.parseLong(addDateStr);
                        String addDate = DATE_FORMAT.format(new Date(timestamp * 1000));
                        bookmarkNode.setColor(addDate);
                    } catch (NumberFormatException e) {
                        bookmarkNode.setColor("时间格式错误");
                    }
                } else {
                    bookmarkNode.setColor("未知时间");
                }

                log.info("{} 追加书签 {}", folderStack.peek().getName(), bookmarkNode.getName());
                folderStack.peek().getChildren().add(bookmarkNode);
            }

            Element dlClose = dt.nextElementSibling();
            if (dlClose != null && dlClose.tagName().equals("dl") && dlClose.html().isEmpty()) {
                log.info("{} 结束解析", folderStack.peek().getName());
                if (folderStack.size() > 1) {
                    folderStack.pop();
                }
            }
        }

        return root;
    }

        // 书签项基类（文件夹/书签通用属性）
        public static abstract class BookmarkNode {
            private String name;
            private String addDate;

            public BookmarkNode(String name, String addDate) {
                this.name = name;
                this.addDate = addDate;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getAddDate() {
                return addDate;
            }

            public void setAddDate(String addDate) {
                this.addDate = addDate;
            }

            public abstract String getType();
        }

        // 书签项（单个链接）
        public static class BookmarkItem extends BookmarkNode {
            private String href;
            private String icon;

            public BookmarkItem(String name, String addDate, String href, String icon) {
                super(name, addDate);
                this.href = href;
                this.icon = icon;
            }

            public String getHref() {
                return href;
            }

            public void setHref(String href) {
                this.href = href;
            }

            public String getIcon() {
                return icon;
            }

            public void setIcon(String icon) {
                this.icon = icon;
            }

            @Override
            public String getType() {
                return "bookmark";
            }

            @Override
            public String toString() {
                return "BookmarkItem{" +
                        "name='" + getName() + '\'' +
                        ", addDate='" + getAddDate() + '\'' +
                        ", href='" + href + '\'' +
                        ", icon='" + (icon != null ? "data:image/..." : "null") + '\'' +
                        '}';
            }
        }

        // 文件夹（包含子节点）
        public static class BookmarkFolder extends BookmarkNode {
            private String lastModified;
            private List<BookmarkNode> children = new ArrayList<>();

            public BookmarkFolder(String name, String addDate, String lastModified) {
                super(name, addDate);
                this.lastModified = lastModified;
            }

            public String getLastModified() {
                return lastModified;
            }

            public void setLastModified(String lastModified) {
                this.lastModified = lastModified;
            }

            public List<BookmarkNode> getChildren() {
                return children;
            }

            public void addChild(BookmarkNode child) {
                this.children.add(child);
            }

            @Override
            public String getType() {
                return "folder";
            }

            @Override
            public String toString() {
                return "BookmarkFolder{" +
                        "name='" + getName() + '\'' +
                        ", addDate='" + getAddDate() + '\'' +
                        ", lastModified='" + lastModified + '\'' +
                        ", childrenCount=" + children.size() +
                        '}';
            }
        }

        // 根节点（整个书签树的根）
        private BookmarkFolder rootFolder;

        public ChromeBookmarkParser() {
            this.rootFolder = new BookmarkFolder("Root", "", "");
        }

        /**
         * 解析书签HTML文件
         *
         * @param filePath 书签文件路径
         * @throws IOException 读取/解析异常
         */
        public void parse(String filePath) throws IOException {
            File file = new File(filePath);
            Document doc = Jsoup.parse(file, "UTF-8");
            // 从第一个DL标签开始解析（Chrome书签的核心内容在DL/DT层级中）
            Elements rootDl = doc.select("DL");
            if (!rootDl.isEmpty()) {
                parseDlNode(rootDl.first(), rootFolder);
            }
        }

        /**
         * 递归解析DL节点（文件夹内容容器）
         *
         * @param dlElement    DL标签元素
         * @param parentFolder 父文件夹
         */
        private void parseDlNode(Element dlElement, BookmarkFolder parentFolder) {
            // 遍历DL下的所有子节点（主要是DT）
            for (Node node : dlElement.childNodes()) {
                if (node instanceof Element dtElement && "DT".equals(dtElement.tagName())) {
                    // 处理DT下的H3（文件夹）或A（书签）
                    Elements h3Elements = dtElement.select("H3");
                    Elements aElements = dtElement.select("A");

                    if (!h3Elements.isEmpty()) {
                        // 解析文件夹
                        parseFolderNode(h3Elements.first(), dtElement, parentFolder);
                    } else if (!aElements.isEmpty()) {
                        // 解析书签
                        parseBookmarkNode(aElements.first(), parentFolder);
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
        private void parseFolderNode(Element h3Element, Element dtElement, BookmarkFolder parentFolder) {
            // 提取文件夹属性
            String folderName = cleanText(h3Element.text());
            String addDate = h3Element.attr("ADD_DATE");
            String lastModified = h3Element.attr("LAST_MODIFIED");

            // 创建文件夹节点
            BookmarkFolder folder = new BookmarkFolder(folderName, addDate, lastModified);
            parentFolder.addChild(folder);

            // 找到文件夹对应的DL节点（DT的下一个兄弟节点中找DL）
            Node nextNode = dtElement.nextSibling();
            while (nextNode != null) {
                if (nextNode instanceof Element nextElement && "DL".equals(nextElement.tagName())) {
                    // 递归解析文件夹内的内容
                    parseDlNode(nextElement, folder);
                    break;
                }
                nextNode = nextNode.nextSibling();
            }
        }

        /**
         * 解析书签节点（A标签）
         *
         * @param aElement     A标签元素
         * @param parentFolder 父文件夹
         */
        private void parseBookmarkNode(Element aElement, BookmarkFolder parentFolder) {
            // 提取书签属性
            String bookmarkName = cleanText(aElement.text());
            String addDate = aElement.attr("ADD_DATE");
            String href = aElement.attr("HREF");
            String icon = aElement.attr("ICON"); // Base64格式的图标

            // 创建书签节点
            BookmarkItem bookmark = new BookmarkItem(bookmarkName, addDate, href, icon);
            parentFolder.addChild(bookmark);
        }

        /**
         * 清理文本（去除不可见字符、多余空格）
         *
         * @param text 原始文本
         * @return 清理后的文本
         */
        private String cleanText(String text) {
            if (text == null) return "";
            // 移除零宽字符、全角空格、多余空格
            return text.replaceAll("[\\u200B\\u200C\\u200D\\uFEFF\\s]+", " ").trim();
        }

        /**
         * 打印书签树（调试用）
         *
         * @param node   起始节点
         * @param indent 缩进（用于层级展示）
         */
        public void printBookmarkTree(BookmarkNode node, String indent) {
            System.out.println(indent + node);
            if (node instanceof BookmarkFolder folder) {
                for (BookmarkNode child : folder.getChildren()) {
                    printBookmarkTree(child, indent + "  ");
                }
            }
        }

        // 获取根文件夹
        public BookmarkFolder getRootFolder() {
            return rootFolder;
        }

        // 测试主方法
        public static void main(String[] args) {
            try {
                ChromeBookmarkParser parser = new ChromeBookmarkParser();
                // 替换为你的书签文件路径
                parser.parse("bookmarks.html");

                // 打印解析结果
                System.out.println("===== Chrome书签解析结果 =====");
                parser.printBookmarkTree(parser.getRootFolder(), "");

            } catch (IOException e) {
                System.err.println("解析书签失败：" + e.getMessage());
                e.printStackTrace();
            }
        }

    }