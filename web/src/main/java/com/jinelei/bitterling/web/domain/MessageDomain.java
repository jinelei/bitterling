package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.RecordDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.web.enums.MessageType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "MESSAGE")
@Schema(title = "消息领域对象", description = "消息领域对象")
public class MessageDomain extends RecordDomain<Long>  {
    @Column(name = "send_user")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotNull(message = "发送人不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "发送人")
    private String sendUser;
    @Column(name = "receive_user")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotNull(message = "接收人不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "接收人")
    private String receiveUser;
    @Column(name = "group_id")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @Schema(description = "消息组")
    private String groupId;
    @Column(name = "title")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotBlank(message = "消息标题不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "消息标题")
    private String title;
    @Column(name = "type")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotNull(message = "消息类型不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "消息类型")
    private MessageType type = MessageType.NOTIFY;
    @Column(name = "content")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotBlank(message = "消息内容不能为空", groups = {BaseView.Persist.class})
    @Schema(description = "消息内容")
    private String content;
    @Column(name = "read_state")
    @JsonView(value = {BaseView.Query.class, BaseView.Create.class, BaseView.Update.class})
    @NotNull(message = "读取状态不能为空", groups = {BaseView.Persist.class})
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

    public String getSendUser() {
        return sendUser;
    }

    public void setSendUser(String sendUser) {
        this.sendUser = sendUser;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getReadState() {
        return readState;
    }

    public void setReadState(Boolean readState) {
        this.readState = readState;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MessageDomain that = (MessageDomain) o;
        return Objects.equals(sendUser, that.sendUser) && Objects.equals(receiveUser, that.receiveUser) && Objects.equals(groupId, that.groupId) && Objects.equals(title, that.title) && type == that.type && Objects.equals(content, that.content) && Objects.equals(readState, that.readState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), sendUser, receiveUser, groupId, title, type, content, readState);
    }

    @Override
    public String toString() {
        return "MessageDomain{" +
                "sendUser='" + sendUser + '\'' +
                ", receiveUser='" + receiveUser + '\'' +
                ", groupId='" + groupId + '\'' +
                ", title='" + title + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", readState=" + readState +
                ", id=" + id +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

}
