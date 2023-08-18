package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.exception.BookingValidException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByEndDesc(Long bookerId, Pageable pageable);

    Page<Booking> findByBookerIdAndStatusOrderByEndDesc(Long bookerId, Status status, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long bookerId,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerOrderByStartDesc(User owner, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerAndStatusOrderByStartDesc(User owner, Status status, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime end, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerAndStartAfterOrderByStartDesc(User owner, LocalDateTime start, Pageable pageable);

    Page<Booking> findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<Booking> findBookingsByBookerAndItemAndEndBeforeAndStatus(User user, Item item, LocalDateTime end, Status status);

    Booking findFirstByItem_IdAndStatusAndStartIsBeforeOrderByEndDesc(Long itemId, Status status, LocalDateTime start);

    Booking findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime start);

    default Booking getBookingById(Long bookingId){
        return findById(bookingId).orElseThrow(() ->
                new BookingValidException("Бронирование не найденно"));
    }
}


