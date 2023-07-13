package ru.practicum.shareit.item.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    @Autowired
    private UserRepository userRepository;

    private Map<Long, Item> ITEMS = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item getById(Long id) {
        return ITEMS.get(id);
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        userRepository.validateUserId(userId);
        return ITEMS.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item saveItem(Item item, Long userId) {
        userRepository.validateUserId(userId);
        item.setId(++id);
        item.setUserId(userId);
        ITEMS.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        userRepository.validateUserId(userId);
        validateItemId(itemId);
        validateItemUserId(userId, itemId);
        Item updatedItem = ITEMS.get(itemId);
        updatedItem.setAvailable(item.getAvailable() == null ? updatedItem.getAvailable() : item.getAvailable());
        updatedItem.setName(item.getName() == null ? updatedItem.getName() : item.getName());
        updatedItem.setDescription(item.getDescription() == null ? updatedItem.getDescription() : item.getDescription());
        updatedItem.setRequest(item.getRequest() == null ? updatedItem.getRequest() : item.getRequest());
        return updatedItem;
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        userRepository.validateUserId(userId);
        validateItemId(itemId);
        ITEMS.remove(itemId);
    }

    @Override
    public Collection<Item> getItemsSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return ITEMS.values().stream().filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void validateItemId(Long itemId) {
        if (ITEMS.values()
                .stream()
                .anyMatch(item -> item.getId().equals(itemId))) {
            return;
        }
        throw new ItemIdValidationException(String.format("Предмета с id %d не существует", itemId));
    }

    private void validateItemUserId(Long userId, Long itemId) {
        if (ITEMS.get(itemId).getUserId().equals(userId)) {
            return;
        }
        throw new ItemDoesNotBelongToUserException(String.format("Предмет с id %d не принадлежит пользователю с id %d", itemId, userId));
    }
}
