package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.booking.validation.BookingDateValid;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@BookingDateValid
public class BookingDtoRequest {
    private Long itemId;
    @NotNull(message = "дата не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    @NotNull(message = "дата не может быть пустой")
    private LocalDateTime end;
}