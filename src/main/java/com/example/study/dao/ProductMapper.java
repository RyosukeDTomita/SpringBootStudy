package com.example.study.dao;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ProductMapper {
  Optional<ProductEntity> findById(@Param("productId") Integer productId);

  void decrementStock(@Param("productId") Integer productId, @Param("quantity") Integer quantity);
}
