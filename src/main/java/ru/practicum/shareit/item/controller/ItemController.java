package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public Collection<Item> getItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получение всех вещей пользователя {}", userId);
        return itemService.getItemsByUser(userId);
    }

    @GetMapping("/search")
    public Collection<Item> getItemsSearch(@RequestParam String text) {
        log.info("Поиск вещей: {}", text);
        return itemService.getItemsSearch(text);
    }


    @GetMapping("/{itemId}")
    public Item getById(@PathVariable Long itemId) {
        return itemService.getById(itemId);
    }

    @PostMapping
    public Item saveItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Сохранение предмета {} пользователя с id {}", item, userId);
        return itemService.saveItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestBody Item item, @PathVariable Long itemId,
                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Изменение предмета с id {} у пользователя {}", itemId, userId);
        return itemService.updateItem(item, itemId, userId);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemService.deleteItem(itemId, userId);
    }
}
