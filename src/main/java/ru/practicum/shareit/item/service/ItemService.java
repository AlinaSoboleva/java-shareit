package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item getById(Long id);

    Item saveItem(Item item, Long userId);

    Item updateItem(Item item, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    List<Item> getItemsByUser(long userId);

    Collection<Item> getItemsSearch(String text);
}
