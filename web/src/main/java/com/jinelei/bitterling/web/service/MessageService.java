package com.jinelei.bitterling.web.service;

import org.springframework.stereotype.Service;

@Service
public class MessageService {

    public long unreadMessageBox() {
        return Math.round(Math.random() * 30.0);
    }

}
