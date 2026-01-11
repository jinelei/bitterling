package com.jinelei.bitterling.core.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @description:
 * @author: 605142
 * @create: 2026/1/11
 * @version v1.0.0
 **/
@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@SuppressWarnings("unused")
public class RecordDomain<ID> extends BaseDomain<ID> implements Comparable<RecordDomain<ID>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "主键ID")
    protected ID id;
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
    public int compareTo(RecordDomain<ID> o) {
        return Optional.ofNullable(o.getOrderNumber())
                .map(s -> s.compareTo(
                        Optional.of(o).map(RecordDomain::getOrderNumber).orElse(0)))
                .orElse(0);
    }
}
