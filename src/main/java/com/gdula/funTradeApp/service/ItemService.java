package com.gdula.funTradeApp.service;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.model.User;
import com.gdula.funTradeApp.repository.ItemRepository;
import com.gdula.funTradeApp.repository.UserRepository;
import com.gdula.funTradeApp.service.dto.CreateUpdateItemDto;
import com.gdula.funTradeApp.service.dto.ItemDto;
import com.gdula.funTradeApp.service.exception.ItemDataInvalid;
import com.gdula.funTradeApp.service.exception.ItemNotFound;
import com.gdula.funTradeApp.service.exception.UserNotFound;
import com.gdula.funTradeApp.service.mapper.ItemDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
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
    @Autowired
    private UserService userService;

    public ItemDto createItem(CreateUpdateItemDto dto, MultipartFile file) throws ItemDataInvalid {
        if (dto.getName() == null || dto.getName().isEmpty() ||
                dto.getPrice() == null || dto.getDescription() == null ||
                dto.getDescription().isEmpty() || dto.getCategory() == null || dto.getShape() == null) {
            throw new ItemDataInvalid();
        }

        Item itemToSave = mapper.toModel(dto);
        User owner = userRepository.findFirstByLogin(securityUtils.getUserName());
        itemToSave.setOwner(owner);

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        System.out.println(fileName);
        try {
            itemToSave.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Item savedItem = itemRepository.save(itemToSave);
        return mapper.toDto(savedItem);
    }

    public ItemDto updateItem(CreateUpdateItemDto dto, String id) throws ItemDataInvalid, ItemNotFound {
        if (dto.getName() == null || dto.getName().isEmpty() ||
                dto.getPrice() == null || dto.getDescription() == null || dto.getDescription().isEmpty() || dto.getCategory() == null || dto.getShape() == null) {
            throw new ItemDataInvalid();
        }

        Item item = itemRepository.findById(id).orElseThrow(ItemNotFound::new);

        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setDescription(dto.getDescription());
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

    public List<ItemDto> getLoggedUserItems() {
        User user = userRepository.findFirstByLogin(securityUtils.getUserName());
        List<Item> items = itemRepository.findAll();
        List<Item> userItems = new ArrayList<>();

        for (Item item : items) {
            if (item.getOwner().equals(user)) {
                userItems.add(item);
            }
        }

        return userItems.stream()
                .map(i -> mapper.toDto(i))
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAllUserItemsByUserId(String id) throws UserNotFound {
        return userService.getUserById(id)
                .getItems()
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

    public List<ItemDto> getAllItemsWithKeyword(String keyword) {
        if (keyword != null) {
            return itemRepository.findAllWithKeyword(keyword)
                    .stream()
                    .map(i -> mapper.toDto(i))
                    .collect(Collectors.toList());
        }
        return getAllItems();
    }
}
