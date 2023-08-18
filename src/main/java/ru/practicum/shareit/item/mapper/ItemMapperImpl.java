package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemResponseDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class ItemMapperImpl {

    public static ItemDto toDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDto itemDto = new ItemDto();

        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequest() != null ? item.getRequest().getId() : null);

        return itemDto;
    }

    public static Item toEntity(ItemDto itemDto, User owner, ItemRequest itemRequest) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setOwner(owner);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setRequest(itemRequest);

        return item;
    }

    public static ItemResponseDto toItemDaoResponse(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        if (item == null) return null;

        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.setId(item.getId());
        itemResponseDto.setName(item.getName());
        itemResponseDto.setDescription(item.getDescription());
        itemResponseDto.setAvailable(item.getAvailable());
        if (item.getRequest() != null)
            itemResponseDto.setRequestId(item.getRequest().getId());

        if (lastBooking != null)
            itemResponseDto.setLastBooking(BookingMapperImpl.toDto(lastBooking));
        if (nextBooking != null)
            itemResponseDto.setNextBooking(BookingMapperImpl.toDto(nextBooking));
        itemResponseDto.setComments(comments.stream().map(CommentMapper.INSTANCE::toCommentDto).collect(Collectors.toList()));

        return itemResponseDto;
    }

}
