package ru.practicum.shareit.request.controller;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServer;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

    @InjectMocks
    private ItemRequestController itemRequestController;

    @Mock
    private ItemRequestServer itemRequestServer;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
    }

    @Test
    @DisplayName("Добавление нового запроса")
    void add() {
        Mockito.when(itemRequestServer.add(itemRequestDto, 1L)).thenReturn(itemRequestDto);

        ResponseEntity<ItemRequestDto> response = itemRequestController.add(itemRequestDto, 1L);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemRequestDto, equalTo(response.getBody()));
    }

    @Test
    @DisplayName("Получение всех запросов пользователя")
    void findAllOwnerRequests() {
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        Mockito.when(itemRequestServer.findAllOwnerRequests(1L)).thenReturn(itemRequestDtoList);

        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.findAllOwnerRequests(1L);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemRequestDtoList, hasSize(response.getBody().size()));
    }

    @Test
    @DisplayName("Получение списка всех запросов")
    void findAll() {
        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
        Mockito.when(itemRequestServer.findAll(1L, 0, 10)).thenReturn(itemRequestDtoList);

        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.findAll(1L, 0, 10);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemRequestDtoList, hasSize(response.getBody().size()));
    }

    @Test
    @DisplayName("Получение конкретного запроса")
    void getRequestById() {
        Mockito.when(itemRequestServer.getRequestById(1L, 1L)).thenReturn(itemRequestDto);

        ResponseEntity<ItemRequestDto> response = itemRequestController.getRequestById(1L, 1L);

        assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
        assertThat(itemRequestDto, equalTo(response.getBody()));
    }
}