package ru.practicum.shareit.item.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemDto {
    private Long id;

    private String name;

    private String description;

    private Boolean available;
    private Long requestId;

}
