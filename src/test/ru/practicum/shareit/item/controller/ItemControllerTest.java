package ru.practicum.shareit.item.controller;

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
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ItemControllerTest {

    @InjectMocks
    private ItemController itemController;

    @Mock
    private ItemService itemService;

    private ItemDto itemDto;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemDto = new ItemDto();
        commentDto = new CommentDto();
    }

    @Test
    @DisplayName("Получение всех предметов пользователя")
    void getItems() {
        List<ItemDto> itemDtos = List.of(itemDto);
        Mockito.when(itemService.getItemsByUser(1l, 0, 10)).thenReturn(itemDtos);

        ResponseEntity<List<ItemDto>> response = itemController.getItems(1l, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasSize(itemDtos.size()));
        assertEquals(response.getBody(), itemDtos);
    }

    @Test
    @DisplayName("Получение списка предметов по поиску")
    void getItemsSearch() {
        List<ItemDto> itemDtos = List.of(itemDto);
        Mockito.when(itemService.getItemsSearch("новый", 1l, 0, 10)).thenReturn(itemDtos);

        ResponseEntity<List<ItemDto>> response = itemController.getItemsSearch("новый", 1l, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody(), hasSize(itemDtos.size()));
        assertEquals(response.getBody(), itemDtos);
    }

    @Test
    @DisplayName("Получение предмета")
    void getById() {
        Mockito.when(itemService.getById(1l, 1l)).thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.getById(1l, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemDto, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Добавление комментария")
    void postComment() {
        Mockito.when(itemService.postComment(commentDto, 1l, 1l)).thenReturn(commentDto);

        ResponseEntity<CommentDto> response = itemController.postComment(commentDto, 1l, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(commentDto, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Сохранение предмета")
    void saveItem() {
        Mockito.when(itemService.saveItem(itemDto, 1l)).thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.saveItem(itemDto, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemDto, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Обновление предмета")
    void updateItem() {
        Mockito.when(itemService.updateItem(itemDto, 1l, 1l)).thenReturn(itemDto);

        ResponseEntity<ItemDto> response = itemController.updateItem(itemDto, 1l, 1l);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemDto, equalTo(response.getBody()));
    }
}