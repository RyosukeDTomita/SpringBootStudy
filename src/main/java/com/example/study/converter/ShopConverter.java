package com.example.study.converter;

import com.example.study.controller.dto.ProductResponse;
import com.example.study.dao.OrderEntity;
import com.example.study.dao.ProductEntity;
import com.example.study.domain.Order;
import com.example.study.domain.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShopConverter {
    Product toProduct(ProductEntity entity);

    ProductResponse toResponse(Product product);

    OrderEntity toEntity(Order order);
}
