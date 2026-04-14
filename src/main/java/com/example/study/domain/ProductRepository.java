package com.example.study.domain;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Integer productId);

    void decrementStock(Integer productId, Integer quantity);
}
