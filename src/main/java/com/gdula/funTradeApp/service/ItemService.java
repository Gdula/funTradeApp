package com.gdula.funTradeApp.service;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.model.User;
import com.gdula.funTradeApp.repository.ItemRepository;
import com.gdula.funTradeApp.repository.UserRepository;
import com.gdula.funTradeApp.service.dto.CreateUpdateItemDto;
import com.gdula.funTradeApp.service.dto.ItemDto;
import com.gdula.funTradeApp.service.exception.ItemDataInvalid;
import com.gdula.funTradeApp.service.exception.ItemNotFound;
import com.gdula.funTradeApp.service.mapper.ItemDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ItemDtoMapper mapper;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserRepository userRepository;

    public ItemDto createItem(CreateUpdateItemDto dto) throws ItemDataInvalid {
        if (dto.getName() == null || dto.getName().isEmpty() ||
                dto.getPrice() == null || dto.getCategory() == null || dto.getShape() == null) {
            throw new ItemDataInvalid();
        }

        Item itemToSave = mapper.toModel(dto);
        User owner = userRepository.findFirstByLogin(securityUtils.getUserName());
        itemToSave.setOwner(owner);
        Item savedItem = itemRepository.save(itemToSave);
        return mapper.toDto(savedItem);
    }

    public ItemDto updateItem(CreateUpdateItemDto dto, String id) throws ItemDataInvalid, ItemNotFound {
        if (dto.getName() == null || dto.getName().isEmpty() ||
                dto.getPrice() == null || dto.getCategory() == null || dto.getShape() == null) {
            throw new ItemDataInvalid();
        }

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFound::new);

        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setCategory(dto.getCategory());
        item.setShape(dto.getShape());
        Item savedItem = itemRepository.save(item);

        return mapper.toDto(savedItem);
    }

    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(i -> mapper.toDto(i))
                .collect(Collectors.toList());
    }

    public ItemDto getItemById(String id) throws ItemNotFound {
        return itemRepository.findById(id)
                .map(i -> mapper.toDto(i))
                .orElseThrow(ItemNotFound::new);
    }

    public ItemDto deleteItemById(String id) throws ItemNotFound {
        Item item = itemRepository.findById(id)
                .orElseThrow(ItemNotFound::new);

        itemRepository.delete(item);

        return mapper.toDto(item);
    }
}
