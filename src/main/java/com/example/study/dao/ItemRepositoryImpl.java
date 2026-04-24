package com.example.study.dao;

import com.example.study.converter.ItemConverter;
import com.example.study.domain.Item;
import com.example.study.domain.ItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ItemRepositoryImpl implements ItemRepository {
    private final ItemMapper itemMapper;
    private final ItemConverter itemConverter;

    public ItemRepositoryImpl(ItemMapper itemMapper, ItemConverter itemConverter) {
        this.itemMapper = itemMapper;
        this.itemConverter = itemConverter;
    }

    @Override
    public List<Item> findAll() {
        return itemMapper.findAll().stream()
                .map(itemConverter::toDomain)
                .toList();
    }

    @Override
    public Optional<Item> findById(Integer itemId) {
        return itemMapper.findById(itemId)
                .map(itemConverter::toDomain);
    }

    @Override
    public Item save(Item item) {
        ItemEntity entity = itemConverter.toEntity(item);
        itemMapper.insert(entity);
        // insertでuseGeneratedKeysにより、entityにitemIdがセットされる
        return itemConverter.toDomain(entity);
    }

    @Override
    public Item update(Item item) {
        ItemEntity entity = itemConverter.toEntity(item);
        itemMapper.update(entity);
        return item;
    }

    @Override
    public void deleteById(Integer itemId) {
        itemMapper.deleteById(itemId);
    }
}
