package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserEmailValidationException;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.mapper.UserMapperImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {


    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User user;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        user = new User();
        userDto = new UserDto();
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void getAllUsers() {
        List<User> users = List.of(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserDto> userDtos = userService.getAllUsers();

        assertThat(users, hasSize(userDtos.size()));
    }

    @Test
    @DisplayName("Получение пользователя по id, если пользователь существует в базе данных")
    void getById_whenUserFount() {
        when(userRepository.getUserById(1L)).thenReturn(user);
        UserDto expectedUser = UserMapperImpl.toDto(user);

        UserDto actualUser = userService.getById(1L);

        assertThat(expectedUser, equalTo(actualUser));
    }

    @Test
    @DisplayName("Получение несуществующего пользователя")
    void getById_whenUserNotFount() {
        when(userRepository.getUserById(1L)).thenThrow(UserIdValidationException.class);

        Exception exception = assertThrows(UserIdValidationException.class, () ->
                userService.getById(1L));

        assertThat(UserIdValidationException.class, equalTo(exception.getClass()));
        verify(userRepository, times(1)).getUserById(anyLong());
    }

    @Test
    @DisplayName("Сохранение пользователя")
    void saveUser_whenInvoked() {
        when(userRepository.save(any(User.class)))
                .thenReturn(UserMapperImpl.toEntity((userDto)));

        UserDto actual = userService.saveUser(userDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertThat(userDto, equalTo(actual));
    }

    @Test
    @DisplayName("Сохранение пользователя при возникновении ошибки")
    void saveUser_whenUserNotSave() {
        when(userRepository.save(any(User.class)))
                .thenThrow(UserEmailValidationException.class);

        Exception exception = assertThrows(UserEmailValidationException.class, () ->
                userService.saveUser(userDto));

        assertThat("Пользователь не был создан: " + userDto, equalTo(exception.getMessage()));
    }

    @Test
    @DisplayName("Обновление данных пользователя")
    void updateUser() {
        userDto.setName("Alina");
        userDto.setEmail("mail@mail.ru");
        when(userRepository.save(any(User.class))).thenReturn(UserMapperImpl.toEntity(userDto));
        when(userRepository.getUserById(1L)).thenReturn(user);

        UserDto actual = userService.updateUser(1L, userDto);

        verify(userRepository, times(1)).save(any(User.class));
        assertThat(userDto, equalTo(actual));
    }
}
