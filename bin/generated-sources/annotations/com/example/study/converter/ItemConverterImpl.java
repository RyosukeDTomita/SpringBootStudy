package com.example.study.converter;

import com.example.study.controller.dto.ItemRequest;
import com.example.study.controller.dto.ItemResponse;
import com.example.study.dao.ItemEntity;
import com.example.study.domain.Item;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-04-27T09:57:05+0900",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.46.0.v20260407-0427, environment: Java 21.0.10 (Eclipse Adoptium)"
)
@Component
public class ItemConverterImpl implements ItemConverter {

    @Override
    public Item toDomain(ItemEntity entity) {
        if ( entity == null ) {
            return null;
        }

        Integer itemId = null;
        String name = null;
        Integer price = null;
        String description = null;

        itemId = entity.getItemId();
        name = entity.getName();
        price = entity.getPrice();
        description = entity.getDescription();

        Item item = new Item( itemId, name, price, description );

        return item;
    }

    @Override
    public ItemEntity toEntity(Item item) {
        if ( item == null ) {
            return null;
        }

        ItemEntity itemEntity = new ItemEntity();

        itemEntity.setItemId( item.getItemId() );
        itemEntity.setName( item.getName() );
        itemEntity.setPrice( item.getPrice() );
        itemEntity.setDescription( item.getDescription() );

        return itemEntity;
    }

    @Override
    public ItemResponse toResponse(Item item) {
        if ( item == null ) {
            return null;
        }

        Integer itemId = null;
        String name = null;
        Integer price = null;
        String description = null;

        itemId = item.getItemId();
        name = item.getName();
        price = item.getPrice();
        description = item.getDescription();

        ItemResponse itemResponse = new ItemResponse( itemId, name, price, description );

        return itemResponse;
    }

    @Override
    public Item toDomain(ItemRequest request) {
        if ( request == null ) {
            return null;
        }

        String name = null;
        Integer price = null;
        String description = null;

        name = request.name();
        price = request.price();
        description = request.description();

        Integer itemId = null;

        Item item = new Item( itemId, name, price, description );

        return item;
    }
}
