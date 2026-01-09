package com.jinelei.bitterling.web.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MessageDomain;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Validator;

@Service
public class MessageService extends BaseService<MessageDomain, Long> {
    protected static String MSG_TITLE_USER_LOGIN = "用户登录";

    public MessageService(BaseRepository<MessageDomain, Long> repository, Validator validator) {
        super(repository, validator);
    }

    public void userLoginNotify(String receiveUser) {
        systemNotify(MSG_TITLE_USER_LOGIN, String.format("登录时间: %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))), receiveUser);
    }

    public void systemNotify(String title, String content, String receiveUser) {
        MessageDomain message = new MessageDomain();
        message.setTitle(
                Optional.ofNullable(title).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
        message.setContent(
                Optional.ofNullable(content).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
        message.setReceiveUser(
                Optional.ofNullable(receiveUser).map(String::trim).orElseThrow(() -> new BusinessException("接收人不能为空")));
        message.setSendUser("system");
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        Set<jakarta.validation.ConstraintViolation<MessageDomain>> violations = validator.validate(message);
        if (!violations.isEmpty()) {
            throw new jakarta.validation.ConstraintViolationException(violations);
        }
    }

    public List<MessageDomain> unreadMessages() {
        List<MessageDomain> all = this.repository.findAll((r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(r.get("readState"), Boolean.TRUE));
            return cb.and(predicates.toArray(Predicate[]::new));
        });
        return all;
    }

    public Long unreadMessagesCount() {
        Long all = this.repository.count((r, q, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(r.get("readState"), Boolean.TRUE));
            return cb.and(predicates.toArray(Predicate[]::new));
        });
        return all;
    }

}
