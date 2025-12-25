package com.jinelei.bitterling.core.service;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.jinelei.bitterling.core.domain.BaseDomain;
import com.jinelei.bitterling.core.repository.BaseRepository;

/**
 * 基础服务
 */
public abstract class BaseService<ENT extends BaseDomain<ID>, ID> {
    protected final BaseRepository<ENT, ID> repository;
    protected final Logger log;

    public BaseService(BaseRepository<ENT, ID> repository) {
        this.repository = repository;
        this.log = LoggerFactory.getLogger(this.getClass());
    }

    /**
     * 新增/保存实体
     * 
     * @param entity 待保存的领域实体
     * @return 保存后的领域实体（携带持久化后的ID等信息）
     */
    public ENT save(ENT entity) {
        Optional.ofNullable(entity).orElseThrow(() -> new IllegalArgumentException("待保存的实体不能为null"));
        ENT savedEntity = repository.save(entity);
        log.info("{}类型实体保存成功，实体ID：{}", savedEntity.getClass().getSimpleName(), savedEntity.getId());
        return savedEntity;
    }

    /**
     * 批量保存实体
     * 
     * @param entities 待批量保存的实体列表
     * @return 批量保存后的实体列表
     */
    public Iterable<ENT> saveAll(Collection<ENT> entities) {
        Optional.ofNullable(entities).filter(c -> !CollectionUtils.isEmpty(c))
                .orElseThrow(() -> new IllegalArgumentException("批量保存实体：传入的实体列表为null或空，无需执行保存操作"));
        Iterable<ENT> savedEntities = repository.saveAll(entities);
        return savedEntities;
    }

    /**
     * 根据ID查询实体
     * 
     * @param id 实体主键ID
     * @return 封装了实体的Optional对象（避免空指针，无数据时返回Optional.empty()）
     */
    public Optional<ENT> findById(ID id) {
        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("查询的主键ID不能为null"));
        Optional<ENT> opt = repository.findById(id);
        return opt;
    }

    /**
     * 查询所有实体
     * 
     * @return 所有实体的列表（无数据时返回空列表，非null）
     */
    public Iterable<ENT> findAll() {
        Iterable<ENT> list = repository.findAll();
        return list;
    }

    /**
     * 根据ID删除实体
     * 
     * @param id 实体主键ID
     */
    public void deleteById(ID id) {
        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("删除的主键ID不能为null"));
        // 先判断实体是否存在
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException(String.format("删除失败：不存在ID为%s的%s实体", id, getEntityClassSimpleName()));
        }
    }

    /**
     * 批量删除实体
     * 
     * @param ids 待删除实体的主键ID列表
     */
    public void deleteAllById(List<ID> ids) {
        List<ID> validIds = Optional.ofNullable(ids)
                .map(c -> c.stream().filter(id -> id != null).toList())
                .filter(c -> !CollectionUtils.isEmpty(c))
                .orElseThrow(() -> new IllegalArgumentException("批量删除实体：传入的ID列表为null或空，无需执行删除操作"));
        repository.deleteAllById(validIds);
    }

    /**
     * 判断实体是否存在（根据ID）
     * 
     * @param id 实体主键ID
     * @return true：存在，false：不存在
     */
    public boolean existsById(ID id) {
        Optional.ofNullable(id).orElseThrow(() -> new IllegalArgumentException("判断存在性的主键ID不能为null"));
        boolean exists = repository.existsById(id);
        return exists;
    }

    /**
     * 查询实体总数
     * 
     * @return 实体总数量
     */
    public long count() {
        long count = repository.count();
        return count;
    }

    /**
     * 获取实体类的简单名称（用于日志输出，简化代码）
     * 
     * @return 实体类简单名称（如User、Order）
     */
    @SuppressWarnings("unchecked")
    private String getEntityClassSimpleName() {
        // 通过泛型推断获取实体类类型
        Class<ENT> clazz = (Class<ENT>) ((ParameterizedType) getClass().getGenericSuperclass())
                .getActualTypeArguments()[0];
        return clazz.getSimpleName();
    }
}