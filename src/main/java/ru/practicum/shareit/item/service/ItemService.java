package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto getById(Long id);

    ItemDto saveItem(Item item, Long userId);

    ItemDto updateItem(Item item, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    List<ItemDto> getItemsByUser(long userId);

    List<ItemDto> getItemsSearch(String text);
}
