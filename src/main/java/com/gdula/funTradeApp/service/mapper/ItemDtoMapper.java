package com.gdula.funTradeApp.service.mapper;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.service.dto.CreateUpdateItemDto;
import com.gdula.funTradeApp.service.dto.ItemDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ItemDtoMapper {

    public ItemDto toDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getPrice(), item.getDescription(), item.getCategory(), item.getShape(), item.getOwner());
    }

    public Item toModel(CreateUpdateItemDto dto) {
        String randomId = UUID.randomUUID().toString();

        return new Item(randomId, dto.getName(), dto.getPrice(), dto.getDescription(), dto.getCategory(), dto.getShape(), dto.getOwner());
    }

    public CreateUpdateItemDto toCreateUpdateItemDto(ItemDto dto) {
        return new CreateUpdateItemDto(dto.getName(), dto.getPrice(), dto.getDescription(), dto.getCategory(), dto.getShape(), dto.getOwner());
    }
}
