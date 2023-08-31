package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
    }

    @Test
    @DisplayName("Получение всех пользователей")
    void getAllUsers_whenInvoked_thenResponseStatusOkWithUsersCollectionsInBody() throws Exception {
        List<UserDto> users = List.of(userDto);
        when(userService.getAllUsers()).thenReturn(users);

        String result = mockMvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(mapper.writeValueAsString(users), equalTo(result));
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Сохранение пользователя")
    void saveUser_whenInvoked_thenResponseStatusOk() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setEmail("mail@mail.ru");
        userDto.setName("Alina");

        when(userService.saveUser(any(UserDto.class))).thenReturn(userDto);

        String result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(mapper.writeValueAsString(userDto), equalTo(result));
    }

    @Test
    @DisplayName("Получение пользователя")
    void getById_whenInvoked_thenResponseStatusOk() throws Exception {
        Mockito.when(userService.getById(1L)).thenReturn(userDto);

        String result = mockMvc.perform(get("/users/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(mapper.writeValueAsString(userDto), equalTo(result));
        verify(userService, times(1)).getById(anyLong());
    }

    @Test
    @DisplayName("Обновление пользователя")
    void updateUser_whenInvoked() throws Exception {

        Mockito.when(userService.updateUser(1L, userDto)).thenReturn(userDto);

        mockMvc.perform(patch("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());
    }
}