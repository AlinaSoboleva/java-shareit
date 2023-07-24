package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ItemRepositoryImpl implements ItemRepository {

    private UserRepository userRepository;

    public ItemRepositoryImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 0L;

    @Override
    public Item getById(Long id) {
        return items.get(id);
    }

    @Override
    public List<Item> findByUserId(Long userId) {
        userRepository.validateUserId(userId);
        return items.values().stream()
                .filter(item -> item.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Item saveItem(Item item, Long userId) {
        userRepository.validateUserId(userId);
        item.setId(++id);
        item.setUserId(userId);
        items.put(id, item);
        return item;
    }

    @Override
    public Item updateItem(Item item, Long itemId, Long userId) {
        userRepository.validateUserId(userId);
        validateItemId(itemId);
        validateItemUserId(userId, itemId);
        Item updatedItem = items.get(itemId);
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
        items.remove(itemId);
    }

    @Override
    public List<Item> getItemsSearch(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return items.values().stream().filter(Item::getAvailable)
                .filter(item ->
                        item.getName().toLowerCase().contains(text.toLowerCase()) ||
                                item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .collect(Collectors.toList());
    }

    private void validateItemId(Long itemId) {
        if (items.values()
                .stream()
                .anyMatch(item -> item.getId().equals(itemId))) {
            return;
        }
        throw new ItemIdValidationException(String.format("Предмета с id %d не существует", itemId));
    }

    private void validateItemUserId(Long userId, Long itemId) {
        if (items.get(itemId).getUserId().equals(userId)) {
            return;
        }
        throw new ItemDoesNotBelongToUserException(String.format("Предмет с id %d не принадлежит пользователю с id %d", itemId, userId));
    }
}
