package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Slf4j
@Controller
@RequestMapping("/items")
@Validated
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems(@RequestHeader(X_SHARER_USER_ID) long userId,
                                                  @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                  @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        log.info("Получение всех вещей пользователя {}", userId);
        return ResponseEntity.ok(itemService.getItemsByUser(userId, from, size));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDto>> getItemsSearch(@RequestParam String text,
                                                        @RequestHeader(X_SHARER_USER_ID) Long userId,
                                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        log.info("Поиск вещей: {}", text);
        return ResponseEntity.ok(itemService.getItemsSearch(text, userId, from, size));
    }


    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getById(@PathVariable Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return new ResponseEntity<>(itemService.getById(itemId, userId), HttpStatus.OK);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<CommentDto> postComment(@Valid @RequestBody CommentDto commentDto, @PathVariable Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return new ResponseEntity<>(itemService.postComment(commentDto, itemId, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ItemDto> saveItem(@Valid @RequestBody ItemDto item,
                                            @RequestHeader(X_SHARER_USER_ID) long userId) {
        log.info("Сохранение предмета {} пользователя с id {}", item, userId);
        return ResponseEntity.ok(itemService.saveItem(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@RequestBody ItemDto itemDto, @PathVariable Long itemId,
                                              @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Изменение предмета с id {} у пользователя {}", itemId, userId);
        return new ResponseEntity<>(itemService.updateItem(itemDto, itemId, userId), HttpStatus.OK);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        itemService.deleteItem(itemId, userId);
    }
}
