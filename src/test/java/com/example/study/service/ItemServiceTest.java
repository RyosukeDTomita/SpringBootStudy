package com.example.study.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.study.domain.Item;
import com.example.study.domain.ItemRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

  @Mock private ItemRepository itemRepository;

  private ItemService itemService;

  @BeforeEach
  void setUp() {
    itemService = new ItemService(itemRepository);
  }

  @Test
  void findAll_アイテム一覧を返す() {
    var items = List.of(new Item(1, "りんご", 100, "青森産"), new Item(2, "みかん", 80, "愛媛産"));
    when(itemRepository.findAll()).thenReturn(items);

    List<Item> result = itemService.findAll();

    assertEquals(2, result.size());
    assertEquals("りんご", result.get(0).getName());
    verify(itemRepository).findAll();
  }

  @Test
  void findById_存在するIDの場合はアイテムを返す() {
    var item = new Item(1, "りんご", 100, "青森産");
    when(itemRepository.findById(1)).thenReturn(Optional.of(item));

    Item result = itemService.findById(1);

    assertEquals("りんご", result.getName());
    assertEquals(100, result.getPrice());
  }

  @Test
  void findById_存在しないIDの場合は例外を投げる() {
    when(itemRepository.findById(999)).thenReturn(Optional.empty());

    RuntimeException ex = assertThrows(RuntimeException.class, () -> itemService.findById(999));
    assertTrue(ex.getMessage().contains("999"));
  }

  @Test
  void save_アイテムを保存して返す() {
    var item = new Item(null, "ぶどう", 300, "山梨産");
    var saved = new Item(3, "ぶどう", 300, "山梨産");
    when(itemRepository.save(item)).thenReturn(saved);

    Item result = itemService.save(item);

    assertEquals(3, result.getItemId());
    assertEquals("ぶどう", result.getName());
  }

  @Test
  void update_存在するアイテムを更新して返す() {
    var existing = new Item(1, "りんご", 100, "青森産");
    var input = new Item(null, "りんご（訳あり）", 80, "青森産");
    var updated = new Item(1, "りんご（訳あり）", 80, "青森産");

    when(itemRepository.findById(1)).thenReturn(Optional.of(existing));
    when(itemRepository.update(any(Item.class))).thenReturn(updated);

    Item result = itemService.update(1, input);

    assertEquals("りんご（訳あり）", result.getName());
    assertEquals(80, result.getPrice());
  }

  @Test
  void update_存在しないアイテムの場合は例外を投げる() {
    var input = new Item(null, "りんご", 100, "青森産");
    when(itemRepository.findById(999)).thenReturn(Optional.empty());

    assertThrows(RuntimeException.class, () -> itemService.update(999, input));
    verify(itemRepository, never()).update(any());
  }

  @Test
  void deleteById_リポジトリのdeleteByIdを呼ぶ() {
    itemService.deleteById(1);

    verify(itemRepository).deleteById(1);
  }
}
