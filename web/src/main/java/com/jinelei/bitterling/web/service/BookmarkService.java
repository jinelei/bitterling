package com.jinelei.bitterling.web.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.BookmarkDomain;
import com.jinelei.bitterling.web.helper.BookmarkParser;

@Service
public class BookmarkService extends BaseService<BookmarkDomain, Long> {

    public BookmarkService(BaseRepository<BookmarkDomain, Long> repository) {
        super(repository);
    }

    public String importFromFile(MultipartFile file) throws IOException {
        List<BookmarkDomain> bookmarkList = BookmarkParser.parseFromFile(file);
        // 打印解析结果
        System.out.println("解析书签总数：" + bookmarkList.size());
        for (BookmarkDomain domain : bookmarkList) {
            System.out.printf("[%s] %s -> %s (创建时间：%s)%n",
                    domain.getType(), domain.getName(), domain.getUrl(), domain.getCreateTime());
        }
        return "success";
    }
}
