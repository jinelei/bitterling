package com.jinelei.bitterling.core.domain.request;

import com.jinelei.bitterling.core.constant.PageablePropertity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "分页请求对象")
@SuppressWarnings("rawtypes")
public class PageableRequest<T> {
    @Schema(name = "分页页码")
    protected Integer pageNo = PageablePropertity.DEFAULT_PAGE_NO;
    @Schema(name = "分页大小")
    protected Integer pageSize = PageablePropertity.DEFAULT_PAGE_SIZE;
    @Schema(name = "查询条件")
    protected T query;

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

    public T getQuery() {
        return query;
    }

    public void setQuery(T query) {
        this.query = query;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((pageNo == null) ? 0 : pageNo.hashCode());
        result = prime * result + ((pageSize == null) ? 0 : pageSize.hashCode());
        result = prime * result + ((query == null) ? 0 : query.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PageableRequest other = (PageableRequest) obj;
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
        if (query == null) {
            if (other.query != null)
                return false;
        } else if (!query.equals(other.query))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "PageableRequest [pageNo=" + pageNo + ", pageSize=" + pageSize + ", query=" + query + "]";
    }

}