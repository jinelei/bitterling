package com.jinelei.bitterling.core.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.jinelei.bitterling.core.domain.view.TreeView;

public class TreeHelper {
    private static final Logger log = LoggerFactory.getLogger(TreeHelper.class);

    /**
     * 列表转树形列表
     * 
     * @param <T>  类型
     * @param <ID> ID
     * @param list 列表
     * @return 根节点
     */
    public static <T extends TreeView<T, ID>, ID> List<T> convertToTree(List<T> list) {
        log.info("列表转树形列表，输入: {}", list);
        if (Objects.isNull(list)) {
            return null;
        }
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        final List<T> rootNodes = new ArrayList<>();
        final Map<ID, List<T>> childrenMap = new HashMap<>();
        list.forEach(it -> {
            if (Objects.isNull(it.getParentId())) {
                rootNodes.add(it);
            } else {
                List<T> collect = childrenMap.getOrDefault(it.getParentId(),
                        new ArrayList<>());
                collect.add(it);
                childrenMap.put(it.getParentId(), collect);
            }
        });
        log.info("列表转树形列表，根节点: {}", rootNodes);
        log.info("列表转树形列表，子节点: {}", childrenMap);
        list.stream()
                .filter(i -> Objects.nonNull(i.getId()))
                .forEach(it -> {
                    Optional.ofNullable(childrenMap.get(it.getId())).ifPresent(it::setChildren);
                });
        log.info("列表转树形列表，组装树: {}", list);
        return rootNodes;
    }

}
