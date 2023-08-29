package ru.practicum.shareit.booking.controller;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static ru.practicum.shareit.booking.enumeration.State.ALL;

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
        Mockito.when(bookingService.approved(1L, true, 1L)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.approved(1L, true, 1L);

        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Сохранение бронирования")
    void saveBooking_whenInvoked() {
        Mockito.when(bookingService.saveBooking(bookingDtoRequest, 1L)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.save(bookingDtoRequest, 1L);

        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Получние бронирования по id")
    void getBookingById() {
        Mockito.when(bookingService.getBookingById(1L, 1L)).thenReturn(bookingDtoResponse);

        ResponseEntity<BookingDtoResponse> response = bookingController.getBookingById(1L, 1L);

        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Получние списка всех бронирований владельца")
    void getAllBookingsOwners() {
        List<BookingDtoResponse> bookingDtoResponses = Arrays.asList(bookingDtoResponse);
        Mockito.when(bookingService.getAllBookingsOwners(1L, ALL, 0, 10)).thenReturn(bookingDtoResponses);

        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1L, ALL, 0, 10);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        MatcherAssert.assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
        Assertions.assertEquals(response.getBody(), bookingDtoResponses);
    }

    @Test
    @DisplayName("Получние всех бронирований")
    void getAllBookingsBookers() {
        List<BookingDtoResponse> bookingDtoResponses = Arrays.asList(bookingDtoResponse);
        Mockito.when(bookingService.getAllBookingsOwners(1L, ALL, 0, 10)).thenReturn(bookingDtoResponses);

        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1L, ALL, 0, 10);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        MatcherAssert.assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
        Assertions.assertEquals(response.getBody(), bookingDtoResponses);
    }
}