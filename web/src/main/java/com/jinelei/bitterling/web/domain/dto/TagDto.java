package com.jinelei.bitterling.web.domain.dto;

import java.util.Objects;

/**
 * 标签
 *
 * @author jinelei
 * @version 1.0.0
 * @date 2026-01-10
 */
public class TagDto {
    private Long id;
    private String icon;
    private String name;
    private Integer count;

    public TagDto() {
    }

    public TagDto(Long id, String icon, String name, Integer count) {
        this.id = id;
        this.icon = icon;
        this.name = name;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TagDto tagDto = (TagDto) o;
        return Objects.equals(id, tagDto.id) && Objects.equals(icon, tagDto.icon) && Objects.equals(name, tagDto.name) && Objects.equals(count, tagDto.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, icon, name, count);
    }

    @Override
    public String toString() {
        return "TagDto{" +
                "id=" + id +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", count=" + count +
                '}';
    }
}
