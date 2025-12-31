package com.jinelei.bitterling.core.domain.view;

import java.util.List;

/**
 * 树状视图
 */
public interface TreeView<T, ID> {
    /**
     * 获取id
     * 
     * @return id
     */
    ID getId();

    /**
     * 获取父级id
     * 
     * @return id
     * 
     */
    ID getParentId();

    /**
     * 获取子级列表
     * 
     * @return 子级列表
     */
    List<T> getChildren();

    /**
     * 设置子级列表
     * 
     * @param list 子级列表
     */
    void setChildren(List<T> list);
}
