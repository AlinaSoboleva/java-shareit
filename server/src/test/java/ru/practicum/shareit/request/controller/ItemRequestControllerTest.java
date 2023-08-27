package ru.practicum.shareit.request.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {

//    @InjectMocks
//    private ItemRequestController itemRequestController;
//
//    @Mock
//    private ItemRequestServer itemRequestServer;
//
//    private ItemRequestDto itemRequestDto;
//
//    @BeforeEach
//    void setUp() {
//        itemRequestDto = new ItemRequestDto();
//    }
//
//    @Test
//    @DisplayName("Добавление нового запроса")
//    void add() {
//        Mockito.when(itemRequestServer.add(itemRequestDto, 1l)).thenReturn(itemRequestDto);
//
//        ResponseEntity<ItemRequestDto> response = itemRequestController.add(itemRequestDto, 1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(itemRequestDto, equalTo(response.getBody()));
//    }
//
//    @Test
//    @DisplayName("Получение все запросов пользователя")
//    void findAllOwnerRequests() {
//        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
//        Mockito.when(itemRequestServer.findAllOwnerRequests(1l)).thenReturn(itemRequestDtoList);
//
//        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.findAllOwnerRequests(1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(itemRequestDtoList, Matchers.hasSize(response.getBody().size()));
//    }
//
//    @Test
//    @DisplayName("Получение списка всех запросов")
//    void findAll() {
//        List<ItemRequestDto> itemRequestDtoList = List.of(itemRequestDto);
//        Mockito.when(itemRequestServer.findAll(1l, 0, 10)).thenReturn(itemRequestDtoList);
//
//        ResponseEntity<List<ItemRequestDto>> response = itemRequestController.findAll(1l, 0, 10);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(itemRequestDtoList, Matchers.hasSize(response.getBody().size()));
//    }
//
//    @Test
//    @DisplayName("Получение конкретного запроса")
//    void getRequestById() {
//        Mockito.when(itemRequestServer.getRequestById(1l, 1l)).thenReturn(itemRequestDto);
//
//        ResponseEntity<ItemRequestDto> response = itemRequestController.getRequestById(1l, 1l);
//
//        MatcherAssert.assertThat(HttpStatus.OK, equalTo(response.getStatusCode()));
//        MatcherAssert.assertThat(itemRequestDto, equalTo(response.getBody()));
//    }
}