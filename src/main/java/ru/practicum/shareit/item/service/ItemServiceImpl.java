package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;

    @Override
    public ItemDto getById(Long id) {
        return ItemMapperImpl.toDto(itemRepository.getById(id));
    }

    @Override
    public List<ItemDto> getItemsByUser(long userId) {
        return itemRepository.findByUserId(userId)
                .stream().map(ItemMapperImpl::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto saveItem(Item item, Long userId) {
        return ItemMapperImpl.toDto(itemRepository.saveItem(item, userId));
    }

    @Override
    public ItemDto updateItem(Item item, Long itemId, Long userId) {
        return ItemMapperImpl.toDto(itemRepository.updateItem(item, itemId, userId));
    }

    @Override
    public void deleteItem(Long itemId, Long userId) {
        itemRepository.deleteItem(itemId, userId);
    }

    @Override
    public List<ItemDto> getItemsSearch(String text) {
        return itemRepository.getItemsSearch(text).stream()
                .map(ItemMapperImpl::toDto).collect(Collectors.toList());
    }
}
