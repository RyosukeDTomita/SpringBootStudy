package com.example.study.dao;

import java.time.LocalDateTime;

public class OrderEntity {
  private Integer orderId;
  private Integer productId;
  private Integer quantity;
  private LocalDateTime orderedAt;

  public OrderEntity() {}

  public OrderEntity(
      Integer orderId, Integer productId, Integer quantity, LocalDateTime orderedAt) {
    this.orderId = orderId;
    this.productId = productId;
    this.quantity = quantity;
    this.orderedAt = orderedAt;
  }

  public Integer getOrderId() {
    return orderId;
  }

  public void setOrderId(Integer orderId) {
    this.orderId = orderId;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  public LocalDateTime getOrderedAt() {
    return orderedAt;
  }

  public void setOrderedAt(LocalDateTime orderedAt) {
    this.orderedAt = orderedAt;
  }
}
