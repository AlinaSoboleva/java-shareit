package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.MatcherAssert;
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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

    @Autowired
    private MockMvc mockMvc;

    private ItemDto itemDto;
    private CommentDto commentDto;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        commentDto = new CommentDto();

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setName("user");
        userDto.setEmail("ali@mail.ru");
    }

    @Test
    @DisplayName("Получение всех предметов пользователя")
    void getItems() throws Exception {
        List<ItemDto> items = List.of(itemDto);
        when(itemService.getItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(items);

        String result = mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MatcherAssert.assertThat(mapper.writeValueAsString(items), equalTo(result));
        verify(itemService, times(1)).getItemsByUser(userDto.getId(), 0, 10);
    }

    @Test
    @DisplayName("Получение списка предметов по поиску")
    void getItemsSearch() throws Exception {
        List<ItemDto> items = List.of(itemDto);
        when(itemService.getItemsSearch(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(items);

        String result = mockMvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .param("from", "0")
                        .param("size", "10")
                        .param("text", "text")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MatcherAssert.assertThat(mapper.writeValueAsString(items), equalTo(result));
        verify(itemService, times(1)).getItemsSearch("text", userDto.getId(), 0, 10);
    }

    @Test
    @DisplayName("Получение предмета")
    void getById() throws Exception {
        Mockito.when(itemService.getById(1L, 1L)).thenReturn(itemDto);

        String result = mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", userDto.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        MatcherAssert.assertThat(mapper.writeValueAsString(itemDto), equalTo(result));
        verify(itemService, times(1)).getById(anyLong(), anyLong());
    }

    @Test
    @DisplayName("Добавление комментария")
    void postComment() throws Exception {
        Mockito.when(itemService.postComment(commentDto, 1L, 1L)).thenReturn(commentDto);

        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);

        commentDto.setText("text");

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 0L)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Сохранение предмета")
    void saveItem() throws Exception {
        Mockito.when(itemService.saveItem(itemDto, 1L)).thenReturn(itemDto);

        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, 0L)
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Обновление предмета")
    void updateItem() throws Exception {
        Mockito.when(itemService.updateItem(itemDto, 1L, 1L)).thenReturn(itemDto);

        itemDto.setName("name");
        itemDto.setDescription("desc");
        itemDto.setAvailable(true);

        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(X_SHARER_USER_ID, String.valueOf(1L))
                        .content(mapper.writeValueAsString(itemDto)))
                .andExpect(status().isOk());
    }
}