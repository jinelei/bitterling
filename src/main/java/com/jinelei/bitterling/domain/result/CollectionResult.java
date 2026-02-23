package com.jinelei.bitterling.domain.result;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Collection;

@Data
@ToString
@EqualsAndHashCode(callSuper = true)
@Schema(name = "CollectionResult", description = "集合响应对象")
@SuppressWarnings("unused")
@JsonPropertyOrder({"code", "message", "total", "data"})
public class CollectionResult<T extends Collection<?>> extends GenericResult<T> {
    @Schema(name = "total", description = "总计")
    protected Long total;

    public CollectionResult() {
        super(CODE_SUCCESS, MESSAGE_SUCCESS, null);
        this.total = 0L;
    }

    public CollectionResult(Integer code, String message, T data) {
        super(code, message, data);
        this.total = (long) data.size();
    }

    public CollectionResult(Integer code, String message, T data, Long total) {
        super(code, message, data);
        this.total = total;
    }
}
