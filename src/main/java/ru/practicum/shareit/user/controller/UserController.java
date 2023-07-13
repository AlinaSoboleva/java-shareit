package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Collection<User> getAllUsers() {
        log.info("Получение всех пользователей");
        return userService.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getById(@PathVariable Long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userService.getById(userId);
    }

    @PostMapping
    public User saveUser(@Valid @RequestBody User user){
        log.info("Сохранение пользователя {}", user);
        return userService.saveUser(user);
    }

    @PatchMapping("/{userId}")
     public User updateUser(@PathVariable Long userId, @RequestBody User user){
        return userService.updateUser(userId, user);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId){
        userService.deleteUser(userId);
    }
}
