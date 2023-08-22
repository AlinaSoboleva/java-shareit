package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank
    @Size(max = 512)
    @Email(message = "некорректный email")
    private String email;
    @Size(max = 255)
    @NotBlank
    private String name;
}
