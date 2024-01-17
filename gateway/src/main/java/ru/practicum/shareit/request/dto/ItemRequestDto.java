package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
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
    @NotBlank
    @Size(max = 512)
    private String description;

    private Set<ItemDto> items = new HashSet<>();
}
