package com.example.study.service;

import com.example.study.domain.Item;
import com.example.study.domain.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    public Item findById(Integer itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));
    }

    public Item save(Item item) {
        return itemRepository.save(item);
    }

    public Item update(Integer itemId, Item item) {
        // 存在確認
        itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));
        Item updated = new Item(itemId, item.getName(), item.getPrice(), item.getDescription());
        return itemRepository.update(updated);
    }

    public void deleteById(Integer itemId) {
        itemRepository.deleteById(itemId);
    }
}
