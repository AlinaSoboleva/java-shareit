package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserService {
    Collection<User> getAllUsers();

    User getById(Long id);

    User saveUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

}
