package ru.practicum.shareit.booking.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;

@Component
@AllArgsConstructor
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

        BookingDto bookingDto = new BookingDto();

        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setItem(ItemMapperImpl.toDto(booking.getItem()));
        bookingDto.setBookerId(booking.getBooker().getId());
        bookingDto.setStatus(booking.getStatus());

        return bookingDto;
    }

    public static BookingDtoResponse toBookingDtoResponse(Booking booking) {
        BookingDtoResponse bookingDtoResponse = new BookingDtoResponse();
        bookingDtoResponse.setId(booking.getId());
        bookingDtoResponse.setStatus(booking.getStatus());
        bookingDtoResponse.setStart(booking.getStart());
        bookingDtoResponse.setEnd(booking.getEnd());
        bookingDtoResponse.setItem(ItemMapperImpl.toDto(booking.getItem()));
        bookingDtoResponse.setBooker(UserMapperImpl.toDto(booking.getBooker()));
        return bookingDtoResponse;
    }
}

