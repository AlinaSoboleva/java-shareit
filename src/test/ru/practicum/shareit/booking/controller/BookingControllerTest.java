package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

    @InjectMocks
    private BookingController bookingController;

    @Mock
    private BookingService bookingService;

    private BookingDtoResponse bookingDtoResponse;
    private BookingDtoRequest bookingDtoRequest;

    @BeforeEach
    void setUp() {
        bookingDtoResponse = new BookingDtoResponse();
        bookingDtoRequest = new BookingDtoRequest();
    }

    @Test
    @DisplayName("Подтверждение бронирования")
    void approved() {
        Mockito.when(bookingService.approved(1l, true, 1l)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.approved(1l, true, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Сохранение бронирования")
    void saveBooking_whenInvoked() {
        Mockito.when(bookingService.saveBooking(bookingDtoRequest, 1l)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.save(bookingDtoRequest, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Получние бронирования по id")
    void getBookingById() {
        Mockito.when(bookingService.getBookingById(1l, 1L)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.getBookingById(1l, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Получние списка всех бронирований владельца")
    void getAllBookingsOwners() {
        List<BookingDtoResponse> bookingDtoResponses = List.of(bookingDtoResponse);
        Mockito.when(bookingService.getAllBookingsOwners(1l, "ALL", 0, 10)).thenReturn(bookingDtoResponses);

        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1l, "ALL", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
        assertEquals(response.getBody(), bookingDtoResponses);
    }

    @Test
    @DisplayName("Получние всех бронирований")
    void getAllBookingsBookers() {
        List<BookingDtoResponse> bookingDtoResponses = List.of(bookingDtoResponse);
        Mockito.when(bookingService.getAllBookingsOwners(1l, "ALL", 0, 10)).thenReturn(bookingDtoResponses);

        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1l, "ALL", 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
        assertEquals(response.getBody(), bookingDtoResponses);
    }
}