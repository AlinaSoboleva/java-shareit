package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDaoResponse;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
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
        itemDto.setRequest(item.getRequest() != null ? item.getRequest().getId() : null);

        return itemDto;
    }

    public static Item toEntity(ItemDto itemDto, User owner) {
        if (itemDto == null) {
            return null;
        }
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setOwner(owner);
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());

        return item;
    }

    public static ItemDaoResponse toItemDaoResponse(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        if (item == null) return null;

        ItemDaoResponse itemDaoResponse = new ItemDaoResponse();

        itemDaoResponse.setId(item.getId());
        itemDaoResponse.setName(item.getName());
        itemDaoResponse.setDescription(item.getDescription());
        itemDaoResponse.setAvailable(item.getAvailable());

        if (lastBooking != null)
            itemDaoResponse.setLastBooking(BookingMapperImpl.toDto(lastBooking));
        if (nextBooking != null)
            itemDaoResponse.setNextBooking(BookingMapperImpl.toDto(nextBooking));
        itemDaoResponse.setComments(comments.stream().map(CommentMapper.INSTANCE::toCommentDto).collect(Collectors.toList()));

        return itemDaoResponse;
    }

}
