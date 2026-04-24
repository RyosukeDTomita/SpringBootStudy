package com.example.study.controller;

import com.example.study.controller.dto.ItemRequest;
import com.example.study.controller.dto.ItemResponse;
import com.example.study.converter.ItemConverter;
import com.example.study.domain.Item;
import com.example.study.service.ItemService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController = @Controller + @ResponseBody
// 戻り値がHttpMessageConverterによって自動的にJSONに変換される
@RestController
@RequestMapping("/register/item")
public class ItemRestController {
    private final ItemService itemService;
    private final ItemConverter itemConverter;

    public ItemRestController(ItemService itemService, ItemConverter itemConverter) {
        this.itemService = itemService;
        this.itemConverter = itemConverter;
    }

    // GET /register/item — 全件取得
    @GetMapping
    public List<ItemResponse> findAll() {
        return itemService.findAll().stream()
                .map(itemConverter::toResponse)
                .toList();
    }

    // GET /register/item/{itemId} — 1件取得
    @GetMapping("/{itemId}")
    public ItemResponse findById(@PathVariable Integer itemId) {
        Item item = itemService.findById(itemId);
        return itemConverter.toResponse(item);
    }

    // POST /register/item — 登録
    // @RequestBodyにより、リクエストのJSON本文がHttpMessageConverterでItemRequestに自動変換される
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemResponse create(@RequestBody ItemRequest request) {
        Item item = itemConverter.toDomain(request);
        Item saved = itemService.save(item);
        return itemConverter.toResponse(saved);
    }

    // PUT /register/item/{itemId} — 更新
    @PutMapping("/{itemId}")
    public ItemResponse update(@PathVariable Integer itemId, @RequestBody ItemRequest request) {
        Item item = itemConverter.toDomain(request);
        Item updated = itemService.update(itemId, item);
        return itemConverter.toResponse(updated);
    }

    // DELETE /register/item/{itemId} — 削除
    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer itemId) {
        itemService.deleteById(itemId);
    }
}
