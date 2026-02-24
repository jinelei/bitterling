package com.jinelei.bitterling.domain.base;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @version v1.0.0
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuppressWarnings("unused")
@Schema(name = "TreeRecordDomain", description = "树形记录基类")
public class TreeRecordDomain<ID> extends BaseDomain<ID> implements Comparable<TreeRecordDomain<ID>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "id", description = "主键ID")
    protected ID id;
    @Schema(description = "父级ID")
    protected ID parentId;
    @Column(name = "create_time")
    @Schema(name = "createTime", description = "创建时间")
    protected LocalDateTime createTime;
    @Column(name = "update_time")
    @Schema(name = "updateTime", description = "更新时间")
    protected LocalDateTime updateTime;
    @Column(name = "order_number")
    @Schema(name = "orderNumber", description = "排序值")
    protected Integer orderNumber = 0;

    @Override
    public int compareTo(TreeRecordDomain<ID> o) {
        return Optional.ofNullable(o.getOrderNumber())
                .map(s -> s.compareTo(
                        Optional.of(o).map(TreeRecordDomain::getOrderNumber).orElse(0)))
                .orElse(0);
    }

    public ID getId() {
        return id;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public ID getParentId() {
        return parentId;
    }

    public void setParentId(ID parentId) {
        this.parentId = parentId;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }
}
