package ru.practicum.shareit.booking.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

public class BookingMapperImpl {
    public static Booking toEntity(BookingDtoRequest bookingDtoRequest, Item item, User user) {
        if (bookingDtoRequest == null) return null;

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStart(bookingDtoRequest.getStart());
        booking.setEnd(bookingDtoRequest.getEnd());
        booking.setStatus(Status.WAITING);
        return booking;
    }

    public static BookingDto toDto(Booking booking) {
        if (booking == null) {
            return null;
        }

        return BookingDto.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapperImpl.toDto(booking.getItem()))
                .bookerId(booking.getBooker() == null ? null : booking.getBooker().getId())
                .build();
    }

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        if (booking == null) return null;

        return BookingDtoResponse.builder()
                .id(booking.getId())
                .status(booking.getStatus())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemMapperImpl.toDto(booking.getItem()))
                .booker(UserMapperImpl.toDto(booking.getBooker()))
                .build();
    }
}

