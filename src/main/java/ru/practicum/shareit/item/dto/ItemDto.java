package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Setter
@Getter
@EqualsAndHashCode
@ToString
public class ItemDto {
    private Long id;

    @NotEmpty
    @NotNull
    private String name;
    @NotEmpty
    @NotNull
    private String description;
    @NotNull
    private Boolean available;
    private Long requestId;

}
