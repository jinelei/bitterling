package com.jinelei.bitterling.web.domain;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonView;
import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.domain.view.BaseView;
import com.jinelei.bitterling.core.exception.BusinessException;
import com.jinelei.bitterling.web.enums.MessageType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "MESSAGE")
@Schema(title = "消息领域对象", description = "消息领域对象")
public class MessageDomain extends BaseDomain<Long> implements Comparable<MessageDomain> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(value = { BaseView.Query.class, BaseView.Delete.class, BaseView.Update.class })
    @NotNull(message = "ID不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "主键ID")
    private Long id;
    @Column(name = "send_user")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "发送人不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "发送人")
    private String sendUser;
    @Column(name = "receive_user")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "接收人不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "接收人")
    private String receiveUser;
    @Column(name = "group_id")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @Schema(description = "消息组")
    private String groupId;
    @Column(name = "title")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotBlank(message = "消息标题不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "消息标题")
    private String title;
    @Column(name = "type")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "消息类型不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "消息类型")
    private MessageType type = MessageType.NOTIFY;
    @Column(name = "content")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotBlank(message = "消息内容不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "消息内容")
    private String content;
    @Column(name = "read_state")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "读取状态不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "读取状态")
    private Boolean readState = Boolean.FALSE;
    @Column(name = "create_time")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "创建时间不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Column(name = "update_time")
    @JsonView(value = { BaseView.Query.class, BaseView.Create.class, BaseView.Update.class })
    @NotNull(message = "更新时间不能为空", groups = { BaseView.Persist.class })
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

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

    @Override
    public int compareTo(MessageDomain o) {
        return Optional.ofNullable(o.getCreateTime())
                .map(s -> s.compareTo(
                        Optional.ofNullable(o).map(MessageDomain::getCreateTime).orElse(LocalDateTime.now())))
                .orElse(0);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.id);
        hash = 11 * hash + Objects.hashCode(this.sendUser);
        hash = 11 * hash + Objects.hashCode(this.receiveUser);
        hash = 11 * hash + Objects.hashCode(this.groupId);
        hash = 11 * hash + Objects.hashCode(this.title);
        hash = 11 * hash + Objects.hashCode(this.type);
        hash = 11 * hash + Objects.hashCode(this.content);
        hash = 11 * hash + Objects.hashCode(this.readState);
        hash = 11 * hash + Objects.hashCode(this.createTime);
        hash = 11 * hash + Objects.hashCode(this.updateTime);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MessageDomain other = (MessageDomain) obj;
        if (!Objects.equals(this.sendUser, other.sendUser)) {
            return false;
        }
        if (!Objects.equals(this.receiveUser, other.receiveUser)) {
            return false;
        }
        if (!Objects.equals(this.groupId, other.groupId)) {
            return false;
        }
        if (!Objects.equals(this.title, other.title)) {
            return false;
        }
        if (!Objects.equals(this.content, other.content)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.readState, other.readState)) {
            return false;
        }
        if (!Objects.equals(this.createTime, other.createTime)) {
            return false;
        }
        return Objects.equals(this.updateTime, other.updateTime);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MessageBoxDomain{");
        sb.append("id=").append(id);
        sb.append(", sendUser=").append(sendUser);
        sb.append(", receiveUser=").append(receiveUser);
        sb.append(", groupId=").append(groupId);
        sb.append(", title=").append(title);
        sb.append(", type=").append(type);
        sb.append(", content=").append(content);
        sb.append(", readState=").append(readState);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append('}');
        return sb.toString();
    }

}
