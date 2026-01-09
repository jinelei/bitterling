package com.jinelei.bitterling.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.core.repository.BaseRepository;
import com.jinelei.bitterling.core.service.BaseService;
import com.jinelei.bitterling.web.domain.MessageBoxDomain;

import jakarta.persistence.criteria.Predicate;

@Service
public class MessageService extends BaseService<MessageBoxDomain, Long> {

    public MessageService(BaseRepository<MessageBoxDomain, Long> repository) {
        super(repository);
    }

    public void systemNotify(String title, String content) {
        MessageBoxDomain message = new MessageBoxDomain();
        message.setTitle(
                Optional.ofNullable(title).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
        message.setContent(
                Optional.ofNullable(content).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
                message.setGroupId(content);
    }

    public List<MessageBoxDomain> unreadMessages() {
        List<MessageBoxDomain> all = this.repository.findAll((r, q, cb) -> {
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
