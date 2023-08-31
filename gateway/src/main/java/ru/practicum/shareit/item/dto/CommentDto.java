package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class CommentDto {
    private Long id;

    @NotBlank
    @Size(max = 2042)
    private String text;

    private String authorName;

    private LocalDateTime created;
}