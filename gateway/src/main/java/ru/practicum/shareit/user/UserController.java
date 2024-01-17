package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.Valid;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Получение всех пользователей");
        return userClient.findAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getById(@PathVariable Long userId) {
        log.info("Получение пользователя с id {}", userId);
        return userClient.getUserById(userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveUser(@Valid @RequestBody UserDto userDto) {
        log.info("Сохранение пользователя {}", userDto);
        return userClient.saveUser(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@PathVariable Long userId, @RequestBody UserDto userDto) {
        log.info("Изменение полей  пользователя с id {}", userId);
        return userClient.updateUser(userId, userDto);
    }

    @DeleteMapping("/{userId}")
    @ResponseBody
    public void deleteUser(@PathVariable Long userId) {
        log.info("Удаление пользователя {}", userId);
        userClient.deleteUser(userId);
    }
}
