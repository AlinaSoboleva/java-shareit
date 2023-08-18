package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsers_whenInvoked_thenResponseStatusOkWithUsersCollectionsInBody() {
        List<UserDto> userDtos = List.of(userDto);
        Mockito.when(userService.getAllUsers()).thenReturn(userDtos);

        ResponseEntity<List<UserDto>> response = userController.getAllUsers();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasSize(userDtos.size()));
        assertEquals(response.getBody(), userDtos);
    }

    @Test
    @DisplayName("Сохранение пользователя")
    void saveUser_whenInvoked_thenResponseStatusOk() {
        userDto.setId(1l);
        Mockito.when(userService.saveUser(userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> responseUser = userController.saveUser(userDto);

        assertThat(HttpStatus.OK, equalTo(responseUser.getStatusCode()));
        assertThat(userDto, equalTo(responseUser.getBody()));
    }


    @Test
    @DisplayName("Получение пользователя")
    void getById_whenInvoked_thenResponseStatusOk() {
        userDto.setId(1l);
        Mockito.when(userService.getById(1l)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.getById(1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(userDto, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser_whenInvoked() {
        userDto.setName("Alina");
        Mockito.when(userService.updateUser(1L, userDto)).thenReturn(userDto);

        ResponseEntity<UserDto> response = userController.updateUser(1l, userDto);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(userDto, equalTo(response.getBody()));
    }
}