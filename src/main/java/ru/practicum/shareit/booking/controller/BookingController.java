package ru.practicum.shareit.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.State;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Controller
@AllArgsConstructor
@RequestMapping(path = "/bookings")
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDtoResponse> approved(@PathVariable Long bookingId, @RequestParam Boolean approved, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return new ResponseEntity<>(bookingService.approved(bookingId, approved, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookingDtoResponse> save(@Valid @RequestBody BookingDtoRequest bookingDtoRequest, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return new ResponseEntity<>(bookingService.saveBooking(bookingDtoRequest, userId), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDtoResponse> getBookingById(@PathVariable Long bookingId, @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return new ResponseEntity<>(bookingService.getBookingById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public ResponseEntity<List<BookingDtoResponse>> getAllBookingsOwners(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestParam(defaultValue = "ALL") State state, @RequestParam(defaultValue = "0") @Min(0) Integer from, @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return ResponseEntity.ok(bookingService.getAllBookingsOwners(userId, state, from, size));
    }

    @GetMapping
    public ResponseEntity<List<BookingDtoResponse>> getAllBookingsBookers(@RequestHeader(X_SHARER_USER_ID) Long userId, @RequestParam(defaultValue = "ALL") State state, @RequestParam(defaultValue = "0") @Min(0) Integer from, @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return ResponseEntity.ok(bookingService.getAllBookingsBookers(userId, state, from, size));
    }
}
