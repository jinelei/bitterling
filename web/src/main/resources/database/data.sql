insert into BOOKMARK (id, parent_id, name, type, order_number, icon, color, create_time, update_time)
values (10000, null, '个人', 0, 1, 'fa-user', 'red', now(), now()),
       (10001, null, '工作', 0, 1, 'fa-briefcase', 'green', now(), now());

insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color, create_time, update_time)
values (10000, 'Cockpit', 1, 2, 'https://home.jinelei.com:9443', 'fa-support', 'red', now(), now()),
       (10000, 'Windows', 1, 3, 'https://docker.jinelei.com:9443', 'fa-windows', 'red', now(), now()),
       (10000, 'Music', 1, 4, 'https://music.jinelei.com:9443', 'fa-music', 'red', now(), now()),
       (10000, 'Video', 1, 5, 'https://video.jinelei.com:9443', 'fa-film', 'red', now(), now()),
       (10000, 'Code', 1, 6, 'https://code.jinelei.com:9443', 'fa-code', 'red', now(), now()),
       (10000, 'BiliBili', 1, 7, 'https://www.bilibili.com/', 'fa-youtube-play', 'red', now(), now()),
       (10000, '豆包', 1, 8, 'https://www.doubao.com/chat/', 'fa-wheelchair-alt', 'red', now(), now()),
       (10000, '开源中国', 1, 9, 'https://www.oschina.net/', 'fa-git-square', 'red', now(), now());


insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color, create_time, update_time)
values (10001, '天行平台-生产', 1, 10, 'https://erdp.seres.cn', 'fa-windows', 'green', now(), now()),
       (10001, '天行平台-UAT', 1, 11, 'https://erdpuat.seres.cn', 'fa-windows', 'green', now(), now()),
       (10001, '天行平台-QA', 1, 12, 'https://erdpqa.seres.cn', 'fa-windows', 'green', now(), now()),
       (10001, '运维门户', 1, 13, 'https://ad-erdp.seres.cn/ops/', 'fa-windows', 'green', now(), now()),
       (10001, 'Nacos ST', 1, 14, 'https://nacos-prod-st.erdp-sd.seres.cn/nacos/#', 'fa-paperclip', 'green', now(),
        now()),
       (10001, 'Nacos QA', 1, 15, 'http://10.81.129.156:8848/nacos/#', 'fa-paperclip', 'green', now(), now()),
       (10001, 'Rancher ST', 1, 16, 'https://rancher.erdp-sd.seres.cn/dashboard/home', 'fa-server', 'green', now(),
        now()),
       (10001, 'DBGate', 1, 17, 'https://dbm.erdp-sd.seres.cn/', 'fa-database', 'green', now(), now()),
       (10001, '禅道', 1, 18, 'https://ztpms.sokon.com:1443/my.html', 'fa-bug', 'green', now(), now()),
       (10001, 'XXL Job QA', 1, 19, 'http://10.81.129.156:8002/xxl-job-admin/', 'fa-file-code-o', 'green', now(),
        now()),
       (10001, 'XXL Job Prod', 1, 20, 'https://xxl-job-admin-prod-st.erdp-sd.seres.cn/xxl-job-admin/', 'fa-file-code-o',
        'green', now(), now()),
       (10001, 'GitLab', 1, 21, 'https://git.seres.cn/erdp/business/dqs/erdp-fas', 'fa-gitlab', 'green', now(), now()),
       (10001, 'SkyWalking', 1, 22, 'https://skywalking-prod.erdp-sd.seres.cn/General-Service/Services', 'fa-arrows',
        'green', now(), now()),
       (10001, 'Nexus', 1, 23, 'https://repo.seres.cn/nexus/#browse/browse', 'fa-sitemap', 'green', now(), now()),
       (10001, 'Grafana', 1, 24, 'https://grafana.erdp-sd.seres.cn/', 'fa-pie-chart', 'green', now(), now()),
       (10001, 'Sonar', 1, 25, 'https://erdp.seres.cn/erdp-sonar/projects?search=erdp-fas', 'fa-pie-chart', 'green',
        now(), now()),
       (10001, '账号查询', 1, 26, 'https://erdpqa.seres.cn/chandao-api/index.html', 'fa-user-secret', 'green', now(),
        now());

insert into MEMO_TAG (id, title, create_time, update_time)
values (1, '工作', now(), now()),
       (2, '生活', now(), now()),
       (3, '学习', now(), now());

insert into MEMO (id, title, sub_title, content, create_time, update_time)
values (1, '测试标题', '测试副标题', '# 内容标题1', now(), now()),
       (2, '测试标题2', '测试副标题2', '# 内容标题2', now(), now()),
       (3, '测试标题3', '测试副标题3', '# 内容标题3', now(), now());

insert into MEMO_TAG_RELATE (memo_id, tag_id, create_time, update_time)
values (1, 1, now(), now()),
       (1, 2, now(), now()),
       (1, 3, now(), now()),
       (2, 1, now(), now()),
       (2, 2, now(), now()),
       (2, 3, now(), now()),
       (3, 3, now(), now())


-- insert into MEMO (id, title, sub_title, content)
-- values (1, '测试标题', "测试副标题", '
--            # 一级标题
--            ## 二级标题
--            ### 三级标题
--            #### 四级标题
--            ##### 五级标题
--            ###### 六级标题
--
--            ## 1. 文本基础格式
--          这是普通文本，包含**加粗文本**、*斜体文本*、***加粗斜体***、~~删除线文本~~。
--          这是<u>下划线文本</u>（HTML 扩展），这是==标记文本==（GFM 扩展）。
--          上标：H~2~O（水），下标：X^2^+Y^2^=Z^2^（勾股定理）。
--
-- ## 2. 引用/块引用
--          > 一级引用：人生苦短，我用 Python。
--          >> 二级嵌套引用：Java 也不错～
--          >>> 三级嵌套引用：Markdown 转换全搞定！
--
-- ## 3. 列表
--            ### 3.1 无序列表
--          - 无序列表项 1
--          - 嵌套无序列表项 1.1
--          - 嵌套无序列表项 1.2
--          - 无序列表项 2
--          * 嵌套无序列表项 2.1（星号）
--          + 嵌套无序列表项 2.2（加号）
--
-- ### 3.2 有序列表
--          1. 有序列表项 1
--          1. 嵌套有序列表项 1.1
--          2. 嵌套有序列表项 1.2
--          2. 有序列表项 2
--          3. 有序列表项 3
--
--            ### 3.3 任务列表（GFM 扩展）
--          - [x] 已完成任务 1
--          - [ ] 未完成任务 2
--          - [x] 已完成任务 3（带备注）
--
-- ## 4. 链接与图片
--            ### 4.1 普通链接
--            [百度首页](https://www.baidu.com "百度-全球最大的中文搜索引擎")
--            [相对路径链接](./docs/api.md)
--          <https://www.github.com>（自动链接）
--
--            ### 4.2 图片
--          ![示例图片](https://picsum.photos/800/400 "随机示例图片")
--          ![缺失图片](https://picsum.photos/404 "缺失图片占位符")
--          ![带链接的图片](https://picsum.photos/200/200 "点击跳转百度")(https://www.baidu.com)
--
--            ## 5. 代码块
--            ### 5.1 单行代码
--          `System.out.println("Hello Markdown!");`（Java 单行代码）
--          `SELECT * FROM user WHERE id = 1;`（SQL 单行代码）
--
-- ### 5.2 多行代码块（带语言标识）');
