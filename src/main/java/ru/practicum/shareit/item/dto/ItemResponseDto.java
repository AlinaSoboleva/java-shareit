package ru.practicum.shareit.item.dto;


import lombok.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ItemResponseDto extends ItemDto {

    private BookingDto nextBooking;
    private BookingDto lastBooking;
    private List<CommentDto> comments;
}
