package ru.practicum.shareit.item.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class CommentDto {
    Long id;
    @NotNull
    @NotBlank
    @NotEmpty
    String text;

    String authorName;

    LocalDateTime created;
}
