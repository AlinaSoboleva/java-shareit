package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public Item getById(Long id) {
        return itemRepository.getById(id);
    }

    @Override
    public List<Item> getItemsByUser(long userId) {
        return itemRepository.findByUserId(userId);
    }

    @Override
    public Item saveItem(Item item, Long userId) {
        return itemRepository.saveItem(item, userId);
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        return itemRepository.updateItem(item, itemId, userId);
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public Collection<Item> getItemsSearch(String text) {
        return itemRepository.getItemsSearch(text);
    }
}
