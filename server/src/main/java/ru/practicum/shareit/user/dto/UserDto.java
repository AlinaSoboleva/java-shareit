package ru.practicum.shareit.user.dto;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserDto {
    private Long id;

    private String email;

    private String name;
}