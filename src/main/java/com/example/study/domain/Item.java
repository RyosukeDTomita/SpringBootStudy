package com.example.study.domain;

public class Item {
  private final Integer itemId;
  private final String name;
  private final Integer price;
  private final String description;

  public Item(Integer itemId, String name, Integer price, String description) {
    this.itemId = itemId;
    this.name = name;
    this.price = price;
    this.description = description;
  }

  public Integer getItemId() {
    return itemId;
  }

  public String getName() {
    return name;
  }

  public Integer getPrice() {
    return price;
  }

  public String getDescription() {
    return description;
  }
}
