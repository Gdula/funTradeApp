package com.gdula.funTradeApp.view;

import com.gdula.funTradeApp.service.UserService;
import com.gdula.funTradeApp.service.dto.CreateUserDto;
import com.gdula.funTradeApp.service.dto.UpdateUserDto;
import com.gdula.funTradeApp.service.dto.UserDto;
import com.gdula.funTradeApp.service.exception.UserAlreadyExists;
import com.gdula.funTradeApp.service.exception.UserDataInvalid;
import com.gdula.funTradeApp.service.exception.UserNotFound;
import com.gdula.funTradeApp.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserViewController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserDtoMapper userDtoMapper;

    @GetMapping("/users")
    public ModelAndView displayUsersTable() {
        ModelAndView mav = new ModelAndView("users-table");
        List<UserDto> allUsers = userService.getAllUsers();
        mav.addObject("users", allUsers);

        return mav;
    }

    @GetMapping("/create-user")
    public String displayCreateUserForm(Model model) {
        CreateUserDto dto = new CreateUserDto();
        model.addAttribute("dto", dto);

        return "create-user-form";
    }

    @PostMapping("/create-user")
    public String createUser(@Valid @ModelAttribute(name = "dto") CreateUserDto dto, BindingResult bindingResult, Model model) {

        if(bindingResult.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            return "create-user-form";
        }
        try {
            userService.createUser(dto);
        } catch (UserDataInvalid | UserAlreadyExists e) {
            // powrót do formularza z tymi samymi danymi
            e.printStackTrace();
            model.addAttribute("dto", dto);
            model.addAttribute("users", userService.getAllUsers());

            return "create-user-form";
        }

        return "redirect:/users";
    }

    @GetMapping("/delete-user/{id}")
    public String deleteUser(@PathVariable String id) {
        try {
            userService.deleteUserById(id);
        } catch (UserNotFound userNotFound) {
            userNotFound.printStackTrace();
        }

        return "redirect:/users";
    }

    @GetMapping("/update-user/{id}")
    public ModelAndView displayUpdateUserForm(@PathVariable String id) {

        try {
            UserDto userById = userService.getUserById(id);

            UpdateUserDto updateUserDto = userDtoMapper.toUpdateDto(userById);
            ModelAndView mav = new ModelAndView("update-user-form");
            mav.addObject("dto", updateUserDto);
            mav.addObject("id", id);
            return mav;

        } catch (UserNotFound userNotFound) {
            return new ModelAndView("redirect:/users");
        }
    }

    @PostMapping("/update-user/{id}")
    public String updateUser(@ModelAttribute UpdateUserDto dto, @PathVariable String id) {

        try {
            userService.updateUser(dto, id);
            return "redirect:/users";
        } catch (UserDataInvalid | UserNotFound e) {
            return "redirect:/update-user/" + id;
        }
    }

    @RequestMapping("/search-user")
    public ModelAndView showUserSearchResult(@Param("keyword") String keyword) {
        List<UserDto> users = userService.getAllUsersWithKeyword(keyword);
        ModelAndView mav = new ModelAndView("users-table");
        mav.addObject("users", users);
        return mav;
    }
}
