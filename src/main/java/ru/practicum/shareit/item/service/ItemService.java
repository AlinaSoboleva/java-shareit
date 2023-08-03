package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto getById(Long id, Long userId);

    ItemDto saveItem(ItemDto itemDto, Long userId);

    ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    List<ItemDto> getItemsByUser(Long userId);

    List<ItemDto> getItemsSearch(String text, Long userId);

    CommentDto postComment(CommentDto commentDto, Long itemId, Long userId);
}
