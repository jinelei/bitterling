package com.jinelei.bitterling.core.domain.result;

import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.jinelei.bitterling.core.constant.PageablePropertity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "分页响应对象")
@SuppressWarnings("rawtypes")
@JsonPropertyOrder({ "code", "message", "pageNo", "pageSize", "total", "data" })
public class PageableResult<T extends Collection> extends GenericResult<T> {
    public static final Integer DEFAULT_TOTAL = 0;
    @Schema(name = "分页页码")
    protected Integer pageNo;
    @Schema(name = "分页大小")
    protected Integer pageSize;
    @Schema(name = "总计")
    protected Integer total;

    public static <T extends Collection> PageableResult<T> of(T data) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(PageablePropertity.DEFAULT_PAGE_NO);
        result.setPageSize(PageablePropertity.DEFAULT_PAGE_SIZE);
        result.setTotal(data instanceof Collection ? data.size() : DEFAULT_TOTAL);
        result.setData(data);
        return result;
    }

    public static <T extends Collection> PageableResult<T> of(T data, Integer total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(PageablePropertity.DEFAULT_PAGE_NO);
        result.setPageSize(PageablePropertity.DEFAULT_PAGE_SIZE);
        result.setTotal(total);
        result.setData(data);
        return result;
    }

    public static <T extends Collection> PageableResult<T> of(Integer pageNo, Integer pageSize, T data) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(data instanceof Collection ? data.size() : DEFAULT_TOTAL);
        result.setData(data);
        return result;
    }

    public static <T extends Collection> PageableResult<T> of(Integer pageNo, Integer pageSize, T data, Integer total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(CODE_SUCCESS);
        result.setMessage(MESSAGE_SUCCESS);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setData(data);
        return result;
    }

    public static <T extends Collection> PageableResult<T> of(Integer code, String message, Integer pageNo,
            Integer pageSize, T data, Integer total) {
        PageableResult<T> result = new PageableResult<>();
        result.setCode(code);
        result.setMessage(message);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setTotal(total);
        result.setData(data);
        return result;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((pageNo == null) ? 0 : pageNo.hashCode());
        result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
        result = prime * result + ((total == null) ? 0 : total.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageableResult other = (PageableResult) obj;
        if (pageNo == null) {
            if (other.pageNo != null)
                return false;
        } else if (!pageNo.equals(other.pageNo))
            return false;
        if (pageSize == null) {
            if (other.pageSize != null)
                return false;
        } else if (!pageSize.equals(other.pageSize))
            return false;
        if (total == null) {
            if (other.total != null)
                return false;
        } else if (!total.equals(other.total))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PageableResult [code=" + code + ", message=" + message + ", pageNo=" + pageNo + ", pageSize=" + pageSize
                + ", total=" + total + ", data=" + data + "]";
    }

}
