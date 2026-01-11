package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.web.enums.MessageType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "MESSAGE")
@Schema(title = "消息领域对象", description = "消息领域对象")
public class MessageDomain extends RecordDomain<Long> {
    @Column(name = "send_user")
    @Schema(description = "发送人")
    private String sendUser;
    @Column(name = "receive_user")
    @Schema(description = "接收人")
    private String receiveUser;
    @Column(name = "group_id")
    @Schema(description = "消息组")
    private String groupId;
    @Column(name = "title")
    @Schema(description = "消息标题")
    private String title;
    @Column(name = "type")
    @Schema(description = "消息类型")
    private MessageType type = MessageType.NOTIFY;
    @Column(name = "content")
    @Schema(description = "消息内容")
    private String content;
    @Column(name = "read_state")
    @Schema(description = "读取状态")
    private Boolean readState = Boolean.FALSE;

    public static MessageDomain ofSystemNotify(String title, String content, String receiveUser) {
        final MessageDomain message = new MessageDomain();
        message.setTitle(
                Optional.ofNullable(title).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
        message.setContent(
                Optional.ofNullable(content).map(String::trim).orElseThrow(() -> new BusinessException("消息标题不能为空")));
        message.setReceiveUser(
                Optional.ofNullable(receiveUser).map(String::trim).orElseThrow(() -> new BusinessException("接收人不能为空")));
        message.setSendUser("system");
        message.setCreateTime(LocalDateTime.now());
        message.setUpdateTime(LocalDateTime.now());
        return message;
    }

}
