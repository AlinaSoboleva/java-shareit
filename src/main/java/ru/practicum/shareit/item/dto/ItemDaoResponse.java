package ru.practicum.shareit.item.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ItemDaoResponse extends ItemDto {

    private BookingDto nextBooking;
    private BookingDto lastBooking;
    private List<CommentDto> comments;
}
