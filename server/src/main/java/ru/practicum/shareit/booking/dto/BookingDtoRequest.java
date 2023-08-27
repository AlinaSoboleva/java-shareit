package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.validation.BookingDateValid;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@BookingDateValid
@EqualsAndHashCode
public class BookingDtoRequest {
    @NotNull
    private Long itemId;
    @NotNull(message = "дата не может быть пустой")
    @FutureOrPresent
    private LocalDateTime start;

    @FutureOrPresent
    @NotNull(message = "дата не может быть пустой")
    private LocalDateTime end;
}
