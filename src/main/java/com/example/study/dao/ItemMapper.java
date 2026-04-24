package com.example.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface ItemMapper {
    List<ItemEntity> findAll();

    Optional<ItemEntity> findById(@Param("itemId") Integer itemId);

    void insert(ItemEntity entity);

    void update(ItemEntity entity);

    void deleteById(@Param("itemId") Integer itemId);
}
