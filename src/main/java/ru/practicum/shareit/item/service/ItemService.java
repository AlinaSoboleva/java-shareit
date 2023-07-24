package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getById(Long id);

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    List<ItemDto> getItemsByUser(long userId);

    List<ItemDto> getItemsSearch(String text);
}
