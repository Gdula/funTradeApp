package com.gdula.funTradeApp.view;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.service.ItemService;
import com.gdula.funTradeApp.service.dto.CreateUpdateItemDto;
import com.gdula.funTradeApp.service.dto.ItemDto;
import com.gdula.funTradeApp.service.exception.ItemDataInvalid;
import com.gdula.funTradeApp.service.exception.ItemNotFound;
import com.gdula.funTradeApp.service.mapper.ItemDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@Controller
public class ItemViewController {
    @Autowired
    private ItemService itemService;
    @Autowired
    private ItemDtoMapper itemDtoMapper;

    @GetMapping("/items")
    public ModelAndView displayUsersTable() {
        ModelAndView mav = new ModelAndView("items-table");
        List<ItemDto> allItems = itemService.getAllItems();
        mav.addObject("items", allItems);

        return mav;
    }

    @GetMapping("/create-item")
    public String displayCreateItemForm(Model model) {
        CreateUpdateItemDto dto = new CreateUpdateItemDto();
        model.addAttribute("categoryList", Arrays.asList(Item.Category.values()));
        model.addAttribute("shapeList", Arrays.asList(Item.Shape.values()));
        model.addAttribute("dto", dto);

        return "create-item-form";
    }

    @PostMapping("/create-item")
    public String createItem(@Valid @ModelAttribute(name = "dto") CreateUpdateItemDto dto, BindingResult bindingResult, Model model, @RequestParam("file") MultipartFile file) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("items", itemService.getAllItems());
            return "create-item-form";
        }
        try {
            itemService.createItem(dto, file);
        } catch (ItemDataInvalid e) {
            e.printStackTrace();
            model.addAttribute("dto",dto);
            model.addAttribute("items", itemService.getAllItems());

            return "create-item-form";
        }

        return "redirect:/items";
    }

    @GetMapping("/delete-item/{id}")
    public String deleteItem(@PathVariable String id) {
        try {
            itemService.deleteItemById(id);
        } catch (ItemNotFound e) {
            e.printStackTrace();
        }

        return "redirect:/items";
    }

    @GetMapping("/update-item/{id}")
    public ModelAndView displayUpdateItemForm(@PathVariable String id) {
        try {
            ItemDto itemById = itemService.getItemById(id);

            CreateUpdateItemDto updateItemDto = itemDtoMapper.toCreateUpdateItemDto(itemById);
            ModelAndView mav = new ModelAndView("update-item-form");
            mav.addObject("dto", updateItemDto);
            mav.addObject("id", id);
            mav.addObject("categoryList", Arrays.asList(Item.Category.values()));
            mav.addObject("shapeList", Arrays.asList(Item.Shape.values()));
            return mav;
        } catch (ItemNotFound e) {
            return new ModelAndView("redirect:/items");
        }
    }

    @PostMapping("/update-item/{id}")
    public String updateItem(@ModelAttribute CreateUpdateItemDto dto, @PathVariable String id) {
        try {
            itemService.updateItem(dto, id);
            return "redirect:/items";
        } catch (ItemNotFound | ItemDataInvalid e) {
            return "redirect:/update-item/" + id;
        }
    }
}
