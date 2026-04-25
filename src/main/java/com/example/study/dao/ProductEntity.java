package com.example.study.dao;

public class ProductEntity {
  private Integer productId;
  private String name;
  private Integer price;
  private Integer stock;

  public ProductEntity() {}

  public ProductEntity(Integer productId, String name, Integer price, Integer stock) {
    this.productId = productId;
    this.name = name;
    this.price = price;
    this.stock = stock;
  }

  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Integer getPrice() {
    return price;
  }

  public void setPrice(Integer price) {
    this.price = price;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }
}
