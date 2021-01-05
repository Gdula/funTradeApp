package com.gdula.funTradeApp.service;

import com.gdula.funTradeApp.model.User;
import com.gdula.funTradeApp.repository.UserRepository;
import com.gdula.funTradeApp.service.dto.CreateUserDto;
import com.gdula.funTradeApp.service.dto.UpdateUserDto;
import com.gdula.funTradeApp.service.dto.UserDto;
import com.gdula.funTradeApp.service.exception.UserAlreadyExists;
import com.gdula.funTradeApp.service.exception.UserDataInvalid;
import com.gdula.funTradeApp.service.exception.UserNotFound;
import com.gdula.funTradeApp.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto dto) throws UserDataInvalid, UserAlreadyExists {
        if (dto.getLogin() == null || dto.getLogin().isEmpty() ||
            dto.getName() == null || dto.getName().isEmpty() ||
            dto.getSurname() == null || dto.getSurname().isEmpty() ||
            dto.getPassword() == null || dto.getPassword().isEmpty() ||
            dto.getAddress() == null || dto.getAddress().isEmpty() ||
            dto.getCity() == null || dto.getCity().isEmpty() ||
            dto.getZip() == null || dto.getZip().isEmpty()) {
            throw new UserDataInvalid();
        }

        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new UserAlreadyExists();
        }

        User userToSave = mapper.toModel(dto);

        String hashedPass = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(hashedPass);

        User savedUser = userRepository.save(userToSave);
        return mapper.toDto(savedUser);
    }

    public UserDto updateUser(UpdateUserDto dto, String id) throws UserNotFound, UserDataInvalid {
        if (dto.getName() == null || dto.getName().isEmpty() ||
            dto.getSurname() == null || dto.getSurname().isEmpty() ||
            dto.getPassword() == null || dto.getPassword().isEmpty() ||
            dto.getAddress() == null || dto.getAddress().isEmpty() ||
            dto.getCity() == null || dto.getCity().isEmpty() ||
            dto.getZip() == null || dto.getZip().isEmpty()) {
            throw new UserDataInvalid();
        }

        User user = userRepository.findById(id).orElseThrow(UserNotFound::new);

        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setAddress(dto.getAddress());
        User savedUser = userRepository.save(user);

        return mapper.toDto(savedUser);

    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> mapper.toDto(u))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(String id) throws UserNotFound {
        return userRepository.findById(id)
                .map(c -> mapper.toDto(c))
                .orElseThrow(UserNotFound::new);
    }

    public UserDto deleteUserById(String id) throws UserNotFound {
        User user = userRepository.findById(id)
                .orElseThrow(UserNotFound::new);

        userRepository.delete(user);

        return mapper.toDto(user);
    }

}
