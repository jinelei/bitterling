package com.jinelei.bitterling.web.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

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
        log.info("userLoginNotify: {}", receiveUser);
        final MessageDomain message = MessageDomain.ofSystemNotify(MSG_TITLE_USER_LOGIN, String.format("登录时间: %s",
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))), receiveUser);
        Set<jakarta.validation.ConstraintViolation<MessageDomain>> violations = validator.validate(message);
        if (!violations.isEmpty()) {
            throw new jakarta.validation.ConstraintViolationException(violations);
        }
        MessageDomain save = repository.save(message);
        log.info("userLoginNotify: {}", save);
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
