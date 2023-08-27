package ru.practicum.shareit.request.service;


import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServerImpl implements ItemRequestServer{

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto add(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.getUserById(userId);
        ItemRequest itemRequest = ItemRequestMapper.toEntity(itemRequestDto, user);
        return ItemRequestMapper.toDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> findAllOwnerRequests(Long userId) {
        User user = userRepository.getUserById(userId);
        return itemRequestRepository.findAllByRequestor(user)
                .stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> findAll(Long userId, Integer from, Integer size) {
        userRepository.getUserById(userId);
        Sort sortByCreated = Sort.by(Sort.Direction.DESC, "created");
        return itemRequestRepository.findAllByRequestorIdNot(userId,PageRequest.of(from, size, sortByCreated)).
                stream().map(ItemRequestMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        userRepository.getUserById(userId);
        return ItemRequestMapper.toDto(itemRequestRepository.getItemRequestById(requestId));
    }
}
