package ru.practicum.shareit.booking.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookingControllerTest {

//    @InjectMocks
//    private BookingController bookingController;
//
//    @Mock
//    private BookingService bookingService;
//
//    private BookingDtoResponse bookingDtoResponse;
//    private BookingDtoRequest bookingDtoRequest;
//
//    @BeforeEach
//    void setUp() {
//        bookingDtoResponse = new BookingDtoResponse();
//        bookingDtoRequest = new BookingDtoRequest();
//    }
//
//    @Test
//    @DisplayName("Подтверждение бронирования")
//    void approved() {
//        Mockito.when(bookingService.approved(1l, true, 1l)).thenReturn(bookingDtoResponse);
//
//        ResponseEntity<BookingDtoResponse> response = bookingController.approved(1l, true, 1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
//    }
//
//    @Test
//    @DisplayName("Сохранение бронирования")
//    void saveBooking_whenInvoked() {
//        Mockito.when(bookingService.saveBooking(bookingDtoRequest, 1l)).thenReturn(bookingDtoResponse);
//
//        ResponseEntity<BookingDtoResponse> response = bookingController.save(bookingDtoRequest, 1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
//    }
//
//    @Test
//    @DisplayName("Получние бронирования по id")
//    void getBookingById() {
//        Mockito.when(bookingService.getBookingById(1l, 1L)).thenReturn(bookingDtoResponse);
//
//        ResponseEntity<BookingDtoResponse> response = bookingController.getBookingById(1l, 1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(bookingDtoResponse, equalTo(response.getBody()));
//    }
//
//    @Test
//    @DisplayName("Получние списка всех бронирований владельца")
//    void getAllBookingsOwners() {
//        List<BookingDtoResponse> bookingDtoResponses = Arrays.asList(bookingDtoResponse);
//        Mockito.when(bookingService.getAllBookingsOwners(1l, ALL, 0, 10)).thenReturn(bookingDtoResponses);
//
//        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1l, ALL, 0, 10);
//
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        MatcherAssert.assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
//        Assertions.assertEquals(response.getBody(), bookingDtoResponses);
//    }
//
//    @Test
//    @DisplayName("Получние всех бронирований")
//    void getAllBookingsBookers() {
//        List<BookingDtoResponse> bookingDtoResponses = Arrays.asList(bookingDtoResponse);
//        Mockito.when(bookingService.getAllBookingsOwners(1l, ALL, 0, 10)).thenReturn(bookingDtoResponses);
//
//        ResponseEntity<List<BookingDtoResponse>> response = bookingController.getAllBookingsOwners(1l, ALL, 0, 10);
//
//        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
//        MatcherAssert.assertThat(response.getBody(), hasSize(bookingDtoResponses.size()));
//        Assertions.assertEquals(response.getBody(), bookingDtoResponses);
//    }
}