package ru.practicum.shareit.item.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
//
//    @MockBean
//    private ItemService itemService;
//
//    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    private ItemDto itemDto;
//    private CommentDto commentDto;
//    private UserDto userDto;
//
//    @BeforeEach
//    void setUp() {
//        itemDto = new ItemDto();
//        commentDto = new CommentDto();
//
//        userDto = new UserDto();
//        userDto.setId(1L);
//        userDto.setName("user");
//        userDto.setEmail("ali@mail.ru");
//    }
//
//    @Test
//    @DisplayName("Получение всех предметов пользователя")
//    void getItems() throws Exception {
//        List<ItemDto> items = List.of(itemDto);
//        when(itemService.getItemsByUser(anyLong(), anyInt(), anyInt())).thenReturn(items);
//
//        String result = mockMvc.perform(get("/items")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .param("from", "0")
//                        .param("size", "10")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(items), equalTo(result));
//        verify(itemService, times(1)).getItemsByUser(userDto.getId(), 0, 10);
//    }
//
//    @Test
//    @DisplayName("Получение списка предметов по поиску")
//    void getItemsSearch() throws Exception {
//        List<ItemDto> items = List.of(itemDto);
//        when(itemService.getItemsSearch(anyString(), anyLong(), anyInt(), anyInt())).thenReturn(items);
//
//        String result = mockMvc.perform(get("/items/search")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .param("from", "0")
//                        .param("size", "10")
//                        .param("text", "text")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(items), equalTo(result));
//        verify(itemService, times(1)).getItemsSearch("text", userDto.getId(), 0, 10);
//    }
//
//    @Test
//    @DisplayName("Получение предмета")
//    void getById() throws Exception {
//        Mockito.when(itemService.getById(1l, 1l)).thenReturn(itemDto);
//
//        String result = mockMvc.perform(get("/items/1")
//                        .header("X-Sharer-User-Id", userDto.getId())
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(itemDto), equalTo(result));
//        verify(itemService, times(1)).getById(anyLong(), anyLong());
//    }
//
//    @Test
//    @DisplayName("Добавление комментария")
//    void postComment() throws Exception {
//        Mockito.when(itemService.postComment(commentDto, 1l, 1l)).thenReturn(commentDto);
//
//        itemDto.setName("name");
//        itemDto.setDescription("desc");
//        itemDto.setAvailable(true);
//
//        commentDto.setText("text");
//
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 0l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    @DisplayName("Сохранение предмета")
//    void saveItem() throws Exception {
//        Mockito.when(itemService.saveItem(itemDto, 1l)).thenReturn(itemDto);
//
//        itemDto.setName("name");
//        itemDto.setDescription("desc");
//        itemDto.setAvailable(true);
//
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 0l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void saveItem_whenNameIsNull_thenResponseStatusBadRequest() throws Exception {
//        itemDto.setName(null);
//        itemDto.setDescription("desc");
//        itemDto.setAvailable(true);
//
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 0l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void saveItem_whenDescriptionIsNull_thenResponseStatusBadRequest() throws Exception {
//        itemDto.setName("name");
//        itemDto.setDescription(null);
//        itemDto.setAvailable(true);
//
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 0l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void saveItem_whenAvailableIsNull_thenResponseStatusBadRequest() throws Exception {
//        itemDto.setName("name");
//        itemDto.setDescription("desc");
//        itemDto.setAvailable(null);
//
//        mockMvc.perform(post("/items")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 0l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    @DisplayName("Обновление предмета")
//    void updateItem() throws Exception {
//        Mockito.when(itemService.updateItem(itemDto, 1l, 1l)).thenReturn(itemDto);
//
//        itemDto.setName("name");
//        itemDto.setDescription("desc");
//        itemDto.setAvailable(true);
//
//        mockMvc.perform(patch("/items/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .header(X_SHARER_USER_ID, 1l)
//                        .content(mapper.writeValueAsString(itemDto)))
//                .andExpect(status().isOk());
//    }
}