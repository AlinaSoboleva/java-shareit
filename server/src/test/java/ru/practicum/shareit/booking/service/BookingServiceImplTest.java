package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.State;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.BookingValidException;
import ru.practicum.shareit.booking.mapper.BookingMapperImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static ru.practicum.shareit.booking.enumeration.State.ALL;
import static ru.practicum.shareit.booking.enumeration.State.WAITING;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private final Long userId = 0L;
    private final Long itemId = 0L;
    private final Long bookingId = 0L;

    @Test
    void saveBooking() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(true);
        item.setOwner(user);

        BookingDtoRequest bookingToSave = new BookingDtoRequest();
        bookingToSave.setItemId(itemId);
        bookingToSave.setStart(LocalDateTime.now());
        bookingToSave.setEnd(LocalDateTime.now().plusMinutes(10));

        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.getItemById(anyLong())).thenReturn(item);
        when(bookingRepository.save(any(Booking.class))).thenReturn(BookingMapperImpl.toEntity(bookingToSave, item, user));

        BookingDtoResponse actualBooking = bookingService.saveBooking(bookingToSave, userId);

        assertThat(bookingToSave.getStart(), equalTo(actualBooking.getStart()));
        assertThat(bookingToSave.getEnd(), equalTo(actualBooking.getEnd()));
        assertThat(bookingToSave.getItemId(), equalTo(actualBooking.getItem().getId()));
        assertThat(1L, equalTo(actualBooking.getBooker().getId()));
        assertThat(Status.WAITING, equalTo(actualBooking.getStatus()));

        InOrder inOrder = Mockito.inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(userId);
        inOrder.verify(itemRepository, times(1)).getItemById(itemId);
        inOrder.verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void saveBooking_whenAvailableIsFalse() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(false);
        item.setOwner(user);

        BookingDtoRequest bookingToSave = new BookingDtoRequest();
        bookingToSave.setItemId(itemId);
        bookingToSave.setStart(LocalDateTime.now());
        bookingToSave.setEnd(LocalDateTime.now().plusMinutes(10));

        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.getItemById(anyLong())).thenReturn(item);

        Exception exception = assertThrows(BookingException.class,
                () -> bookingService.saveBooking(bookingToSave, userId));

        assertThat("Предмет с id: " + item.getId() + " недоступен для бронирования", equalTo(exception.getMessage()));
        InOrder inOrder = Mockito.inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(itemRepository, times(1)).getItemById(anyLong());
        inOrder.verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void saveBooking_whenUserIsOwner() {
        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(itemId);
        item.setAvailable(false);
        item.setOwner(user);

        BookingDtoRequest bookingToSave = new BookingDtoRequest();
        bookingToSave.setItemId(itemId);
        bookingToSave.setStart(LocalDateTime.now());
        bookingToSave.setEnd(LocalDateTime.now().plusMinutes(10));

        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.getItemById(anyLong())).thenReturn(item);

        Exception exception = assertThrows(UserIdValidationException.class,
                () -> bookingService.saveBooking(bookingToSave, userId));

        assertThat("Пользователь с id = " + userId + " владелец вещи с id = " + item.getId(), equalTo(exception.getMessage()));
        InOrder inOrder = Mockito.inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(itemRepository, times(1)).getItemById(anyLong());
        inOrder.verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approved_RejectedBookingStatus() {
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);

        Booking oldBooking = new Booking();
        oldBooking.setItem(item);
        oldBooking.setStatus(Status.WAITING);
        when(bookingRepository.getBookingById(anyLong())).thenReturn(oldBooking);

        Booking newBooking = new Booking();
        newBooking.setStatus(Status.REJECTED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);

        BookingDtoResponse actualBooking = bookingService.approved(bookingId, false, userId);

        assertThat(newBooking.getId(), equalTo(actualBooking.getId()));
        assertThat(newBooking.getStart(), equalTo(actualBooking.getStart()));
        assertThat(newBooking.getEnd(), equalTo(actualBooking.getEnd()));
        assertThat(newBooking.getBooker(), equalTo(actualBooking.getBooker()));
        assertThat(newBooking.getStatus(), equalTo(actualBooking.getStatus()));

        verify(bookingRepository, times(1)).getBookingById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void approved_ApprovedBookingStatus() {
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);

        Booking oldBooking = new Booking();
        oldBooking.setItem(item);
        oldBooking.setStatus(Status.WAITING);
        when(bookingRepository.getBookingById(anyLong())).thenReturn(oldBooking);

        Booking newBooking = new Booking();
        newBooking.setStatus(Status.APPROVED);
        when(bookingRepository.save(any(Booking.class))).thenReturn(newBooking);

        BookingDtoResponse actualBooking = bookingService.approved(bookingId, true, userId);

        assertThat(newBooking.getId(), equalTo(actualBooking.getId()));
        assertThat(newBooking.getStart(), equalTo(actualBooking.getStart()));
        assertThat(newBooking.getEnd(), equalTo(actualBooking.getEnd()));
        assertThat(newBooking.getBooker(), equalTo(actualBooking.getBooker()));
        assertThat(newBooking.getStatus(), equalTo(actualBooking.getStatus()));

        verify(bookingRepository, times(1)).getBookingById(anyLong());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    void approved_whenUserIsNotOwner() {
        User user = new User();
        user.setId(1L);
        Item item = new Item();
        item.setOwner(user);
        item.setId(itemId);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        when(bookingRepository.getBookingById(anyLong())).thenReturn(booking);

        Exception exception = assertThrows(BookingValidException.class, () ->
                bookingService.approved(bookingId, true, userId));

        assertThat("Пользователь с id " + userId + " не является владельцем вещи с id " + item.getId(), equalTo(exception.getMessage()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void approved_whenStatusIsApproved() {
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setOwner(user);
        item.setId(itemId);

        Booking booking = new Booking();
        booking.setItem(item);
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.getBookingById(anyLong())).thenReturn(booking);

        Exception exception = assertThrows(BookingException.class, () ->
                bookingService.approved(bookingId, true, userId));

        assertThat("Нельзя изменить статус после подтверждения бронирования", equalTo(exception.getMessage()));
        verify(bookingRepository, never()).save(any(Booking.class));
    }

    @Test
    void getBookingById() {
        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setOwner(user);

        Booking expectedBooking = new Booking();
        expectedBooking.setBooker(user);
        expectedBooking.setItem(item);

        when(bookingRepository.getBookingById(bookingId)).thenReturn(expectedBooking);

        BookingDtoResponse actualBooking = bookingService.getBookingById(bookingId, userId);

        assertThat(BookingMapperImpl.toBookingDtoResponse(expectedBooking), equalTo(actualBooking));
        verify(bookingRepository, times(1)).getBookingById(bookingId);
    }

    @Test
    void getBookingById_ifBookingIsNotConnectedWithUser() {
        User user = new User();
        user.setId(1L);

        Item item = new Item();
        item.setOwner(user);

        Booking expectedBooking = new Booking();
        expectedBooking.setId(bookingId);
        expectedBooking.setBooker(user);
        expectedBooking.setItem(item);

        when(bookingRepository.getBookingById(bookingId)).thenReturn(expectedBooking);

        Exception exception = assertThrows(BookingValidException.class, () ->
                bookingService.getBookingById(bookingId, userId));

        assertThat("Бронирование с id: " + bookingId + " не связанно с пользователем с id " + userId, equalTo(exception.getMessage()));
        verify(bookingRepository, times(1)).getBookingById(bookingId);
    }

    @Test
    void getAllBookingsOwners_whenStateIsAll() {
        List<Booking> expectedBookingsList = of(new Booking(), new Booking());

        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(bookingRepository.findBookingsByItem_OwnerOrderByStartDesc(any(User.class), any(Pageable.class)))
                .thenReturn(expectedBookingsList);

        List<BookingDtoResponse> actualBookings = bookingService
                .getAllBookingsOwners(userId, ALL, 0, 1);

        assertThat(expectedBookingsList.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList()),
                equalTo(actualBookings));
        InOrder inOrder = inOrder(userRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByItem_OwnerOrderByStartDesc(any(User.class), any(Pageable.class));
    }

    @Test
    void getAllBookingsOwners_whenStateIsCurrent() {
        List<Booking> expectedBookingsList = of(new Booking(), new Booking());

        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(bookingRepository.findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(expectedBookingsList);

        List<BookingDtoResponse> actualBookings = bookingService
                .getAllBookingsOwners(userId, State.CURRENT, 0, 1);

        assertThat(expectedBookingsList.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList()),
                equalTo(actualBookings));
        InOrder inOrder = inOrder(userRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllBookingsOwners_whenStateIsPast() {
        List<Booking> expectedBookingsList = of(new Booking(), new Booking());

        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(bookingRepository.findBookingsByItem_OwnerAndEndBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class)))
                .thenReturn(expectedBookingsList);

        List<BookingDtoResponse> actualBookings = bookingService
                .getAllBookingsOwners(userId, State.PAST, 0, 1);

        assertThat(expectedBookingsList.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList()),
                equalTo(actualBookings));
        InOrder inOrder = inOrder(userRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByItem_OwnerAndEndBeforeOrderByStartDesc(any(User.class), any(LocalDateTime.class), any(Pageable.class));
    }

    @Test
    void getAllBookingsOwners_whenStateIsWaiting() {
        List<Booking> expectedBookingsList = of(new Booking(), new Booking());

        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(bookingRepository.findBookingsByItem_OwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(Pageable.class)))
                .thenReturn(expectedBookingsList);

        List<BookingDtoResponse> actualBookings = bookingService
                .getAllBookingsOwners(userId, WAITING, 0, 1);

        assertThat(expectedBookingsList.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList()),
                equalTo(actualBookings));
        InOrder inOrder = inOrder(userRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByItem_OwnerAndStatusOrderByStartDesc(any(User.class), any(Status.class), any(Pageable.class));
    }

    @Test
    void getAllBookingsBookers_whenStateIsAll() {
        List<Booking> expectedBookingsList = of(new Booking(), new Booking());

        when(userRepository.getUserById(anyLong())).thenReturn(new User());
        when(bookingRepository.findByBookerIdOrderByEndDesc(anyLong(), any(Pageable.class)))
                .thenReturn(expectedBookingsList);

        List<BookingDtoResponse> actualBookings = bookingService
                .getAllBookingsBookers(userId, ALL, 0, 1);

        assertThat(expectedBookingsList.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList()),
                equalTo(actualBookings));
        InOrder inOrder = inOrder(userRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(bookingRepository, times(1))
                .findByBookerIdOrderByEndDesc(anyLong(), any(Pageable.class));

    }
}