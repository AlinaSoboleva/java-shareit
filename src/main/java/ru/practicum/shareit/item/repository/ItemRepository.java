package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item getById(Long id);

    Item saveItem(Item item, Long userId);

    Item updateItem(Item item, Long itemId, Long userId);

    void deleteItem(Long itemId, Long userId);

    List<Item> findByUserId(Long userId);

    List<Item> getItemsSearch(String text);
}
