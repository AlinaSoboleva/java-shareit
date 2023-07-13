package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
public class ItemDto {
    private Long id;
    private Long userId;
    private String name;
    private String description;
    private boolean available;
    private Long request;
}
