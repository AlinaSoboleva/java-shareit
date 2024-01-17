package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Controller
@RequestMapping("/items")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemClient itemClient;


    @GetMapping
    public ResponseEntity<Object> getItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                           @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                           @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        log.info("Получение всех вещей пользователя {}", userId);
        return itemClient.getItemsByUser(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemsSearch(@RequestParam String text,
                                                 @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        log.info("Поиск вещей: {}", text);
        return itemClient.getItemsSearch(text, userId, from, size);
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getById(@PathVariable Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemClient.getById(itemId, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long itemId,
                                              @Valid @RequestBody CommentDto commentDto) {
        log.info("Добавлен новый комментарий: {} \n пользователем с id = {} для вещи с id = {}.",
                commentDto, userId, itemId);
        return itemClient.postComment(commentDto, itemId, userId);
    }

    @PostMapping
    public ResponseEntity<Object> saveItem(@Valid @RequestBody ItemDto item,
                                           @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Сохранение предмета {} пользователя с id {}", item, userId);
        return itemClient.saveItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Изменение предмета с id {} у пользователя {}", itemId, userId);
        return itemClient.updateItem(itemDto, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        itemClient.deleteItem(itemId, userId);
    }
}
