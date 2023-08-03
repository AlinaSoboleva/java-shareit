package ru.practicum.shareit.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
public class UserDto {
    private Long id;
    @NotNull
    @Email(message = "некорректный email")
    private String email;
    private String name;
}
