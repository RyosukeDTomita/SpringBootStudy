package com.example.study.dao;

import com.example.study.converter.ShopConverter;
import com.example.study.domain.Product;
import com.example.study.domain.ProductRepository;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {
  private final ProductMapper productMapper;
  private final ShopConverter shopConverter;

  public ProductRepositoryImpl(ProductMapper productMapper, ShopConverter shopConverter) {
    this.productMapper = productMapper;
    this.shopConverter = shopConverter;
  }

  @Override
  public Optional<Product> findById(Integer productId) {
    return productMapper.findById(productId).map(shopConverter::toProduct);
  }

  @Override
  public void decrementStock(Integer productId, Integer quantity) {
    productMapper.decrementStock(productId, quantity);
  }
}
