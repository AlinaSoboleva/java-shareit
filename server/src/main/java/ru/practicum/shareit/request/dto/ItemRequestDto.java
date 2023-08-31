package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class ItemRequestDto {

    private Long id;

    private LocalDateTime created;

    private String description;

    private Set<ItemDto> items = new HashSet<>();

}
