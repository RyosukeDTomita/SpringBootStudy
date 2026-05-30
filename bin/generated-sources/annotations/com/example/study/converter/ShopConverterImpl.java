package com.example.study.converter;

import com.example.study.controller.dto.ProductResponse;
import com.example.study.dao.OrderEntity;
import com.example.study.dao.ProductEntity;
import com.example.study.domain.Order;
import com.example.study.domain.Product;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T09:57:05+0900",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ShopConverterImpl implements ShopConverter {

    @Override
    public Product toProduct(ProductEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Integer productId = null;
        String name = null;
        Integer price = null;
        Integer stock = null;

        productId = entity.getProductId();
        name = entity.getName();
        price = entity.getPrice();
        stock = entity.getStock();

        Product product = new Product( productId, name, price, stock );

        return product;
    }

    @Override
    public ProductResponse toResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        Integer productId = null;
        String name = null;
        Integer price = null;
        Integer stock = null;

        productId = product.getProductId();
        name = product.getName();
        price = product.getPrice();
        stock = product.getStock();

        ProductResponse productResponse = new ProductResponse( productId, name, price, stock );

        return productResponse;
    }

    @Override
    public OrderEntity toEntity(Order order) {
        if ( order == null ) {
            return null;
        }

        OrderEntity orderEntity = new OrderEntity();

        orderEntity.setOrderId( order.getOrderId() );
        orderEntity.setProductId( order.getProductId() );
        orderEntity.setQuantity( order.getQuantity() );
        orderEntity.setOrderedAt( order.getOrderedAt() );

        return orderEntity;
    }
}
