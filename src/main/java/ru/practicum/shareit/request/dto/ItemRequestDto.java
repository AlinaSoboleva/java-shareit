package ru.practicum.shareit.request.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO Sprint add-item-requests.
 */
@Getter
@Builder
@EqualsAndHashCode
public class ItemRequestDto {

    private Long id;

    private LocalDateTime created;
    @NotNull
    @NotEmpty
    private String description;

    private Set<ItemDto> items = new HashSet<>();

}
