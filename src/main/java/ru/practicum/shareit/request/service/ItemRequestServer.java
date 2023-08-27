package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestServer {

    ItemRequestDto add(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> findAllOwnerRequests(Long userId);

    List<ItemRequestDto> findAll(Long userId, Integer from, Integer size);

    ItemRequestDto getRequestById(Long requestId, Long userId);

}
