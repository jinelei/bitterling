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
@Schema(name = "RecordDomain", description = "记录基类")
public class RecordDomain<ID> extends BaseDomain<ID> implements Comparable<RecordDomain<ID>> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(name = "id", description = "主键ID")
    protected ID id;
    @Column(name = "create_time")
    @Schema(name = "createTime", description = "创建时间")
    protected LocalDateTime createTime;
    @Column(name = "update_time")
    @Schema(name = "updateTime", description = "更新时间")
    protected LocalDateTime updateTime;
    @Column(name = "order_number")
    @Schema(name = "orderNumber", description = "排序值")
    protected Integer orderNumber;

    @Override
    public int compareTo(RecordDomain<ID> o) {
        return Optional.ofNullable(o.getOrderNumber())
                .map(s -> s.compareTo(
                        Optional.of(o).map(RecordDomain::getOrderNumber).orElse(0)))
                .orElse(0);
    }
}
