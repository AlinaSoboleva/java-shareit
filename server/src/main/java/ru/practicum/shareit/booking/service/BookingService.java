package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.State;

import java.util.List;

public interface BookingService {

    BookingDtoResponse saveBooking(BookingDtoRequest bookingDtoRequest, Long userId);

    BookingDtoResponse approved(Long bookingId, Boolean approved, Long userId);

    BookingDtoResponse getBookingById(Long bookingId, Long userId);

    List<BookingDtoResponse> getAllBookingsBookers(Long userId, State state, Integer from, Integer size);

    List<BookingDtoResponse> getAllBookingsOwners(Long userId, State state, Integer from, Integer size);

}
