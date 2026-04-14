package com.example.study.domain;

public class Product {
    private final Integer productId;
    private final String name;
    private final Integer price;
    private final Integer stock;

    public Product(Integer productId, String name, Integer price, Integer stock) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public Integer getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }
}
