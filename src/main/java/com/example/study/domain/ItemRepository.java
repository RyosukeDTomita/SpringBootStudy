package com.example.study.domain;

import java.util.List;
import java.util.Optional;

public interface ItemRepository {
    List<Item> findAll();

    Optional<Item> findById(Integer itemId);

    Item save(Item item);

    Item update(Item item);

    void deleteById(Integer itemId);
}
