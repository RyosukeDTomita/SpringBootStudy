package com.example.study.domain;

import java.time.LocalDateTime;

public class Order {
  private final Integer orderId;
  private final Integer productId;
  private final Integer quantity;
  private final LocalDateTime orderedAt;

  public Order(Integer orderId, Integer productId, Integer quantity, LocalDateTime orderedAt) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.orderedAt = orderedAt;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public Integer getProductId() {
    return productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public LocalDateTime getOrderedAt() {
    return orderedAt;
  }
}
