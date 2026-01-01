insert into
    bookmark (id, parent_id, name, type, order_number, icon)
values
    (10000, null, '娱乐', 0, 1, 'fa-magnet'),
    (10001, null, '工作', 0, 1, 'fa-briefcase');

insert into
    bookmark (
        id,
        parent_id,
        name,
        type,
        order_number,
        url,
        icon
    )
values
    (
        1,
        null,
        '个人导航',
        1,
        1,
        'https://jinelei.com:9443',
        'fa-file'
    ),
    (
        2,
        null,
        'Cockpit',
        1,
        2,
        'https://home.jinelei.com:9443',
        'fa-support'
    ),
    (
        3,
        null,
        'Windows',
        1,
        3,
        'https://docker.jinelei.com:9443',
        'fa-windows'
    ),
    (
        4,
        null,
        'Music',
        1,
        4,
        'https://music.jinelei.com:9443',
        'fa-music'
    ),
    (
        5,
        null,
        'Video',
        1,
        5,
        'https://video.jinelei.com:9443',
        'fa-film'
    ),
    (
        6,
        null,
        'Code',
        1,
        6,
        'https://code.jinelei.com:9443',
        'fa-code'
    );

insert into
    bookmark (
        id,
        parent_id,
        name,
        type,
        order_number,
        url,
        icon
    )
values
    (
        7,
        10000,
        'BiliBili',
        1,
        7,
        'https://www.bilibili.com/',
        'fa-youtube-play'
    ),
    (
        8,
        10000,
        '豆包',
        1,
        8,
        'https://www.doubao.com/chat/',
        'fa-wheelchair-alt'
    );


insert into
    bookmark (
        id,
        parent_id,
        name,
        type,
        order_number,
        url,
        icon
    )
values
    (
        9,
        10001,
        'DQS',
        1,
        9,
        'https://www.bilibili.com/',
        'fa-youtube-play'
    );