package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class UserDto {
    private Long id;
    @Email(message = "некорректный email")
    @NotNull
    private String email;
    private String name;
}
