package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();

    UserDto getById(Long id);

    UserDto saveUser(User user);

    UserDto updateUser(Long userId, User user);

    void deleteUser(Long userId);

}
