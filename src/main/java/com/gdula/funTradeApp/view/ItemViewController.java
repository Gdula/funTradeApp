package com.gdula.funTradeApp.view;

import com.gdula.funTradeApp.model.Item;
import com.gdula.funTradeApp.service.ItemService;
import com.gdula.funTradeApp.service.dto.CreateUpdateItemDto;
import com.gdula.funTradeApp.service.dto.ItemDto;
import com.gdula.funTradeApp.service.dto.UserDto;
import com.gdula.funTradeApp.service.exception.ItemDataInvalid;
import com.gdula.funTradeApp.service.exception.ItemNotFound;
import com.gdula.funTradeApp.service.exception.UserNotFound;
import com.gdula.funTradeApp.service.mapper.ItemDtoMapper;
import com.gdula.funTradeApp.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
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
    @Autowired
    private UserDtoMapper userDtoMapper;

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
    public String createItem(@Valid @ModelAttribute(name = "dto") CreateUpdateItemDto dto, BindingResult bindingResult,
                             Model model, @RequestParam("file") MultipartFile file) {
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

    @GetMapping(value = {"/show-item/{id}", "/user-items-table/show-item/{id}", "/show-owner/user-items-table/show-item/{id}"})
    public ModelAndView displayItem(@PathVariable String id) {
        try {
            ItemDto itemById = itemService.getItemById(id);
            String userMail = itemById.getOwner().getMail();
            ModelAndView mav = new ModelAndView("show-item");
            mav.addObject("item", itemById);
            mav.addObject("userMail", userMail);

            return mav;
        } catch (ItemNotFound e) {
            return new ModelAndView("redirect:/items");
        }
    }

    @GetMapping(value = {"/show-owner/{id}", "/show-item/show-owner/{id}"})
    public ModelAndView displayOwnerByItemId(@PathVariable String id) {
        try {
            UserDto user = userDtoMapper.toDto(itemService.getItemById(id).getOwner());

            ModelAndView mav = new ModelAndView("show-user");
            mav.addObject("user", user);
            return mav;
        } catch (ItemNotFound e) {
            return new ModelAndView("redirect:/items");
        }
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

    @GetMapping("/my-items")
    public ModelAndView showLoggedUserItems() {
        List<ItemDto> userItems = itemService.getLoggedUserItems();
        ModelAndView mav = new ModelAndView("logged-user-items-table");
        mav.addObject("items", userItems);

        return mav;
    }

    @GetMapping(value = {"show-owner/user-items-table/{id}", "/user-items-table/{id}", "/show-item/show-owner/user-items-table/{id}"})
    public ModelAndView showUserItems(@PathVariable String id) {
        try {
            List<ItemDto> userItems = itemService.getAllUserItemsByUserId(id);
            ModelAndView mav = new ModelAndView("user-items-table");
            mav.addObject("items", userItems);
            return mav;
        } catch (UserNotFound e) {
            return new ModelAndView("redirect:/user/" + id);
        }

    }

    @RequestMapping("/search-item")
    public ModelAndView showItemSearchResult(@Param("keyword") String keyword) {
        List<ItemDto> items = itemService.getAllItemsWithKeyword(keyword);
        ModelAndView mav = new ModelAndView("items-table");
        mav.addObject("items", items);
        return mav;
    }


}
