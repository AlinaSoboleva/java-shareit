package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@AllArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDtoResponse> approved(@PathVariable Long bookingId,
                                                       @RequestParam Boolean approved, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.approved(bookingId, approved, userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BookingDtoResponse> save(@Valid @RequestBody BookingDtoRequest bookingDtoRequest,
                                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.saveBooking(bookingDtoRequest, userId), HttpStatus.OK);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDtoResponse> getBookingById(@PathVariable Long bookingId,
                                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        return new ResponseEntity<>(bookingService.getBookingById(bookingId, userId), HttpStatus.OK);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getAllBookingsOwners(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                         @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsOwners(userId, state);
    }

    @GetMapping
    public List<BookingDtoResponse> getAllBookingsBookers(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.getAllBookingsBookers(userId, state);
    }
}
