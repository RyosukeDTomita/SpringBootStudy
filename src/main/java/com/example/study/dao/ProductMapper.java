package com.example.study.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface ProductMapper {
    Optional<ProductEntity> findById(@Param("productId") Integer productId);

    void decrementStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
}
