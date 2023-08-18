package ru.practicum.shareit.request.mapper;

import org.mapstruct.Mapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ItemRequestMapper {

    public static ItemRequestDto toDto(ItemRequest itemRequest) {
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(itemRequest.getItems().stream().map(ItemMapperImpl::toDto).collect(Collectors.toSet()))
                .build();
    }

    public static ItemRequest toEntity(ItemRequestDto itemRequestDto, User requestor) {
        if (itemRequestDto == null) return null;

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(itemRequestDto.getId());
        itemRequest.setRequestor(requestor);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setDescription(itemRequestDto.getDescription());
        if (itemRequestDto.getItems() != null)
            itemRequest.setItems(itemRequestDto.getItems().stream().map(itemDto -> ItemMapperImpl.toEntity(itemDto, requestor, itemRequest)).collect(Collectors.toSet()));
        return itemRequest;
    }
}
