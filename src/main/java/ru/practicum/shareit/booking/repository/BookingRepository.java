package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByBookerIdOrderByStartDesc(Long bookerId);

    List<Booking> findByBookerIdAndStatusOrderByEndDesc(Long bookerId, Status status);

    List<Booking> findByBookerIdAndStartIsBeforeAndEndIsAfterOrderByEndDesc(Long bookerId,
                                                                            LocalDateTime start,
                                                                            LocalDateTime end);

    List<Booking> findByBookerIdAndEndIsBeforeOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findByBookerIdAndStartIsAfterOrderByStartDesc(Long bookerId, LocalDateTime end);

    List<Booking> findBookingsByItem_OwnerOrderByStartDesc(User owner);

    List<Booking> findBookingsByItem_OwnerAndStatusOrderByStartDesc(User owner, Status status);

    List<Booking> findBookingsByItem_OwnerAndEndBeforeOrderByStartDesc(User owner, LocalDateTime end);

    List<Booking> findBookingsByItem_OwnerAndStartAfterOrderByStartDesc(User owner, LocalDateTime start);

    List<Booking> findBookingsByItem_OwnerAndStartBeforeAndEndAfterOrderByStartDesc(User owner, LocalDateTime start, LocalDateTime end);

    List<Booking> findBookingsByBookerAndItemAndEndBeforeAndStatus(User user, Item item, LocalDateTime end, Status status);

    Booking findFirstByItem_IdAndStatusAndStartIsBeforeOrderByEndDesc(Long itemId, Status status, LocalDateTime start);

    Booking findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(Long itemId, Status status, LocalDateTime start);
}


