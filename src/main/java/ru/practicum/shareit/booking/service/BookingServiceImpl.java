package ru.practicum.shareit.booking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoRequest;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.BookingStateException;
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

@Service
@AllArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;

    private final UserRepository userRepository;

    private final ItemRepository itemRepository;


    @Transactional
    @Override
    public BookingDtoResponse saveBooking(BookingDtoRequest bookingDtoRequest, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(bookingDtoRequest.getItemId());
        if (item.getOwner().getId().equals(userId)) {
            log.warn("Пользователь с id = " + userId + " владелец вещи с id = " + item.getId());
            throw new UserIdValidationException("Пользователь с id = " + userId + " владелец вещи с id = " + item.getId());
        }
        if (!item.getAvailable()) {
            log.warn("Предмет с id: {} недоступен для бронирования", item.getId());
            throw new BookingException(String.format("Предмет с id: %s недоступен для бронирования", item.getId()));
        }
        try {
            log.info("Сохранение бронирования " + bookingDtoRequest);
            return BookingMapperImpl.toBookingDtoResponse(bookingRepository.save(BookingMapperImpl.toEntity(bookingDtoRequest, item, user)));
        } catch (Exception e) {
            log.warn("Бронирование не было создано: " + bookingDtoRequest);
            throw new UserIdValidationException("Бронирование не было создано: " + bookingDtoRequest);
        }
    }

    @Transactional
    @Override
    public BookingDtoResponse approved(Long bookingId, Boolean approved, Long userId) {
        userRepository.getUserById(userId);
        Booking booking = bookingRepository.getBookingById(bookingId);
        Item item = booking.getItem();
        if (!item.getOwner().getId().equals(userId)) {
            log.warn("Пользователь с id {} не является владельцем вещи с id {}", userId, item.getId());
            throw new BookingValidException("Пользователь с id " + userId + " не является владельцем вещи с id " + item.getId());
        }
        if (booking.getStatus().equals(Status.APPROVED)) {
            log.warn("Нельзя изменить статус после подтверждения бронирования {}", booking);
            throw new BookingException("Нельзя изменить статус после подтверждения бронирования");
        }
        if (approved) {
            booking.setStatus(Status.APPROVED);
            log.info("Статус бронирования {} измненен на {}", booking, Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
            log.info("Статус бронирования {} измненен на {}", booking, Status.REJECTED);
        }
        bookingRepository.save(booking);
        return BookingMapperImpl.toBookingDtoResponse(booking);
    }

    @Override
    public BookingDtoResponse getBookingById(Long bookingId, Long userId) {
        userRepository.getUserById(userId);
        Booking booking = bookingRepository.getBookingById(bookingId);
        if (!booking.getItem().getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            log.warn("Бронирование с id: " + bookingId + " не связанно с пользователем с id " + userId);
            throw new BookingValidException("Бронирование с id: " + bookingId + " не связанно с пользователем с id " + userId);
        }
        log.info("Полеучение бронирования с id {}", bookingId);
        return BookingMapperImpl.toBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> getAllBookingsOwners(Long userId, String state, Integer from, Integer size) {
        User user = userRepository.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        Pageable pageable = PageRequest.of(from, size);
        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository.findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(user, now, now, pageable);
                break;
            case "PAST":
                bookings = bookingRepository.findBookingsByItem_OwnerAndEndBeforeOrderByStartDesc(user, now, pageable);
                break;
            case "FUTURE":
                bookings = bookingRepository.findBookingsByItem_OwnerAndStartAfterOrderByStartDesc(user, now, pageable);
                break;
            case "WAITING":
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusOrderByStartDesc(user, Status.WAITING, pageable);
                break;
            case "REJECTED":
                bookings = bookingRepository.findBookingsByItem_OwnerAndStatusOrderByStartDesc(user, Status.REJECTED, pageable);
                break;
            case "ALL":
                bookings = bookingRepository.findBookingsByItem_OwnerOrderByStartDesc(user, pageable);
                break;
            default:
                log.warn("Unknown state: " + state);
                throw new BookingStateException("Unknown state: " + state);
        }
        log.info("Получение списка бронирований владельцем вещи {}", user);
        return bookings.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> getAllBookingsBookers(Long userId, String state, Integer from, Integer size) {
        userRepository.getUserById(userId);
        LocalDateTime now = LocalDateTime.now();
        List<Booking> bookings;
        PageRequest pageable = PageRequest.of(from, size);
        switch (state.toUpperCase()) {
            case "CURRENT":
                bookings = bookingRepository.findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(userId, now, now, pageable);
                break;
            case "PAST":
                bookings = bookingRepository.findByBookerIdAndEndIsBeforeOrderByStartDesc(userId, now, pageable);
                break;
            case "FUTURE":
                bookings = bookingRepository.findByBookerIdAndStartIsAfterOrderByStartDesc(userId, now, pageable);
                break;
            case "WAITING":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByEndDesc(userId, Status.WAITING, pageable);
                break;
            case "REJECTED":
                bookings = bookingRepository.findByBookerIdAndStatusOrderByEndDesc(userId, Status.REJECTED, pageable);
                break;
            case "ALL":
                bookings = bookingRepository.findByBookerIdOrderByEndDesc(userId, pageable);
                if (bookings.isEmpty()) {
                    pageable = PageRequest.of(from > 0 ? from / size : 0, size);
                    bookings = bookingRepository.findByBookerIdOrderByEndDesc(userId, pageable);
                }
                break;
            default:
                log.warn("Unknown state: " + state);
                throw new BookingStateException("Unknown state: " + state);
        }
        return bookings.stream().map(BookingMapperImpl::toBookingDtoResponse).collect(Collectors.toList());
    }
}
