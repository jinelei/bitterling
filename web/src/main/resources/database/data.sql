insert into BOOKMARK (id, parent_id, name, type, order_number, icon, color)
values (10000, null, '个人', 0, 1, 'fa-user', 'red'),
       (10001, null, '娱乐', 0, 1, 'fa-youtube-play', 'blue'),
       (10002, null, '工作', 0, 1, 'fa-briefcase', 'green');

insert into BOOKMARK (name, type, order_number, url, icon, color)
values ('个人导航', 1, 1, 'https://jinelei.com:9443', 'fa-home', 'grey');

insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color)
values (10000, 'Cockpit', 1, 2, 'https://home.jinelei.com:9443', 'fa-support', 'red'),
       (10000, 'Windows', 1, 3, 'https://docker.jinelei.com:9443', 'fa-windows', 'red'),
       (10000, 'Music', 1, 4, 'https://music.jinelei.com:9443', 'fa-music', 'red'),
       (10000, 'Video', 1, 5, 'https://video.jinelei.com:9443', 'fa-film', 'red'),
       (10000, 'Code', 1, 6, 'https://code.jinelei.com:9443', 'fa-code', 'red');

insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color)
values (10001, 'BiliBili', 1, 7, 'https://www.bilibili.com/', 'fa-youtube-play', 'blue'),
       (10001, '豆包', 1, 8, 'https://www.doubao.com/chat/', 'fa-wheelchair-alt', 'blue');


insert into BOOKMARK (parent_id, name, type, order_number, url, icon, color)
values (10002, 'DQS222水水水水2', 1, 9, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS1', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS2', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS3', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS4', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS5', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS6', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS7', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS8', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS9', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS10', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS11', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS12', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS13', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS14', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS15', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS16', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green'),
       (10002, 'DQS17', 1, 10, 'https://www.bilibili.com/', 'fa-youtube-play', 'green');