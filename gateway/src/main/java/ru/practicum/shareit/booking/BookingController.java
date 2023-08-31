package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.State;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Controller
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
@Slf4j
@Validated
public class BookingController {

    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getAllBookingsByUser(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен список всех бронирований текущего пользователя с id = {}, state = {}, " +
                "from = {}, size = {}.", userId, state, from, size);
        return bookingClient.getAllBookingsByUser(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllBookingsAllItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(name = "state", defaultValue = "ALL") State state,
            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Получен список всех бронирований для всех вещей владельца с id = {}, state = {}, " +
                "from = {}, size = {}.", userId, state, from, size);
        return bookingClient.getAllBookingsAllItemsByOwner(userId, state, from, size);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long bookingId) {
        log.info("Получено бронирование с id = {}, userId={}.", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PostMapping
    public ResponseEntity<Object> saveBooking(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestBody @Valid BookingDtoRequest bookingDtoRequest) {
        log.info("Добавлен новый запрос на бронирование: {}, userId={}.", bookingDtoRequest, userId);
        return bookingClient.saveBooking(userId, bookingDtoRequest);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBooking(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long bookingId, @RequestParam Boolean approved) {
        log.info("Обновлено бронирование с id = {}, userId={}, approved = {}.", bookingId, userId, approved);
        return bookingClient.updateBooking(userId, bookingId, approved);
    }

}
