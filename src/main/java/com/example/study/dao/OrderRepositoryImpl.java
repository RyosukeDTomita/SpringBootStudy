package com.example.study.dao;

import com.example.study.converter.ShopConverter;
import com.example.study.domain.Order;
import com.example.study.domain.OrderRepository;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {
  private final OrderMapper orderMapper;
  private final ShopConverter shopConverter;

  public OrderRepositoryImpl(OrderMapper orderMapper, ShopConverter shopConverter) {
    this.orderMapper = orderMapper;
    this.shopConverter = shopConverter;
  }

  @Override
  public void save(Order order) {
    OrderEntity entity = shopConverter.toEntity(order);
    orderMapper.insert(entity);
  }
}
