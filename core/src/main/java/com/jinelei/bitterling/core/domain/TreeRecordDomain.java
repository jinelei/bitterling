package com.jinelei.bitterling.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 * @version v1.0.0
 **/
@MappedSuperclass
@SuppressWarnings("unused")
public class TreeRecordDomain<ID> extends BaseDomain<ID> implements Comparable<TreeRecordDomain<ID>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    protected ID id;
    @Schema(description = "父级ID")
    protected Long parentId;
    @Column(name = "create_time")
    @Schema(description = "创建时间")
    protected LocalDateTime createTime;
    @Column(name = "update_time")
    @Schema(description = "更新时间")
    protected LocalDateTime updateTime;
    @Column(name = "order_number")
    @Schema(description = "排序值")
    protected Integer orderNumber;

    @Override
    public ID getId() {
        return id;
    }

    @Override
    public void setId(ID id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TreeRecordDomain<?> that = (TreeRecordDomain<?>) o;
        return Objects.equals(id, that.id) && Objects.equals(parentId, that.parentId) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime) && Objects.equals(orderNumber, that.orderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, createTime, updateTime, orderNumber);
    }

    @Override
    public String toString() {
        return "TreeRecordDomain{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", orderNumber=" + orderNumber +
                "} " + super.toString();
    }

    @Override
    public int compareTo(TreeRecordDomain<ID> o) {
        return Optional.ofNullable(o.getOrderNumber())
                .map(s -> s.compareTo(
                        Optional.of(o).map(TreeRecordDomain::getOrderNumber).orElse(0)))
                .orElse(0);
    }
}
