package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
public class CommentDto {
    private Long id;

    private String text;

    private String authorName;

    private LocalDateTime created;
}
