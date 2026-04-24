package com.example.study.converter;

import com.example.study.controller.dto.ItemRequest;
import com.example.study.controller.dto.ItemResponse;
import com.example.study.dao.ItemEntity;
import com.example.study.domain.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemConverter {
    Item toDomain(ItemEntity entity);

    ItemEntity toEntity(Item item);

    ItemResponse toResponse(Item item);

    // ItemRequestにはitemIdが無いため、nullをセットする
    @Mapping(target = "itemId", ignore = true)
    Item toDomain(ItemRequest request);
}
