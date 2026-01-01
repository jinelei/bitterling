insert into BOOKMARK (id, parent_id, name, type, order_number, icon, color)
values (10000, null, '个人', 0, 1, 'fa-user', 'red'),
       (10001, null, '工作', 0, 1, 'fa-briefcase', 'green');

insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color)
values (10000, 'Cockpit', 1, 2, 'https://home.jinelei.com:9443', 'fa-support', 'red'),
       (10000, 'Windows', 1, 3, 'https://docker.jinelei.com:9443', 'fa-windows', 'red'),
       (10000, 'Music', 1, 4, 'https://music.jinelei.com:9443', 'fa-music', 'red'),
       (10000, 'Video', 1, 5, 'https://video.jinelei.com:9443', 'fa-film', 'red'),
       (10000, 'Code', 1, 6, 'https://code.jinelei.com:9443', 'fa-code', 'red'),
       (10000, 'BiliBili', 1, 7, 'https://www.bilibili.com/', 'fa-youtube-play', 'red'),
       (10000, '豆包', 1, 8, 'https://www.doubao.com/chat/', 'fa-wheelchair-alt', 'red'),
       (10000, '开源中国', 1, 9, 'https://www.oschina.net/', 'fa-git-square', 'red');


insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color)
values (10001, '天行平台-生产', 1, 10, 'https://erdp.seres.cn', 'fa-windows', 'green'),
       (10001, '天行平台-UAT', 1, 11, 'https://erdpuat.seres.cn', 'fa-windows', 'green'),
       (10001, '天行平台-QA', 1, 12, 'https://erdpqa.seres.cn', 'fa-windows', 'green'),
       (10001, '运维门户', 1, 13, 'https://ad-erdp.seres.cn/ops/', 'fa-windows', 'green'),
       (10001, 'Nacos ST', 1, 14, 'https://nacos-prod-st.erdp-sd.seres.cn/nacos/#', 'fa-paperclip', 'green'),
       (10001, 'Nacos QA', 1, 15, 'http://10.81.129.156:8848/nacos/#', 'fa-paperclip', 'green'),
       (10001, 'Rancher ST', 1, 16, 'https://rancher.erdp-sd.seres.cn/dashboard/home', 'fa-server', 'green'),
       (10001, 'DBGate', 1, 17, 'https://dbm.erdp-sd.seres.cn/', 'fa-database', 'green'),
       (10001, '禅道', 1, 18, 'https://ztpms.sokon.com:1443/my.html', 'fa-bug', 'green'),
       (10001, 'XXL Job QA', 1, 19, 'http://10.81.129.156:8002/xxl-job-admin/', 'fa-file-code-o', 'green'),
       (10001, 'XXL Job Prod', 1, 20, 'https://xxl-job-admin-prod-st.erdp-sd.seres.cn/xxl-job-admin/', 'fa-file-code-o',
        'green'),
       (10001, 'GitLab', 1, 21, 'https://git.seres.cn/erdp/business/dqs/erdp-fas', 'fa-gitlab', 'green'),
       (10001, 'SkyWalking', 1, 22, 'https://skywalking-prod.erdp-sd.seres.cn/General-Service/Services', 'fa-arrows',
        'green'),
       (10001, 'Nexus', 1, 23, 'https://repo.seres.cn/nexus/#browse/browse', 'fa-sitemap', 'green'),
       (10001, 'Grafana', 1, 24, 'https://grafana.erdp-sd.seres.cn/', 'fa-pie-chart', 'green'),
       (10001, 'Sonar', 1, 25, 'https://erdp.seres.cn/erdp-sonar/projects?search=erdp-fas', 'fa-pie-chart', 'green'),
       (10001, '账号查询', 1, 26, 'https://erdpqa.seres.cn/chandao-api/index.html', 'fa-user-secret', 'green');
