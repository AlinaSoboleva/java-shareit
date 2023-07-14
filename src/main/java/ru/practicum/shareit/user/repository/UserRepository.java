package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();

    User getById(Long id);

    User saveUser(User user);

    User updateUser(Long userId, User user);

    void deleteUser(Long userId);

    void validateUserId(Long userId);
}
