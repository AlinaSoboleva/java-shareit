package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.exception.UserEmailValidationException;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private Map<Long, User> USERS = new HashMap<>();
    private Long id = 0L;

    @Override
    public Collection<User> getAllUsers() {
        return USERS.values();
    }

    @Override
    public User getById(Long id) {
        validateUserId(id);
        return USERS.get(id);
    }

    @Override
    public User saveUser(User user) {
        validateUserEmail(user.getId(), user.getEmail());
        user.setId(++id);
        USERS.put(id, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User user) {
        validateUserEmail(userId, user.getEmail());
        User updatedUser = USERS.get(userId);
        updatedUser.setName(user.getName() == null ? updatedUser.getName() : user.getName());
        updatedUser.setEmail(user.getEmail() == null ? updatedUser.getEmail() : user.getEmail());
        return updatedUser;
    }

    @Override
    public void deleteUser(Long userId) {
        validateUserId(userId);
        USERS.remove(userId);
    }

    private void validateUserEmail(Long userId, String email) {
        if (USERS.values()
                .stream()
                .filter(user -> !user.getId().equals(userId))
                .anyMatch(user -> user.getEmail().equalsIgnoreCase(email))) {
            throw new UserEmailValidationException(String.format("Пользователь с email %s уже существует", email));
        }
    }

    public void validateUserId(Long userId) {
        if (USERS.values()
                .stream()
                .anyMatch(user -> user.getId().equals(userId))) {
            return;
        }
        throw new UserIdValidationException(String.format("Пользователя с id %d не существует", userId));
    }

}
