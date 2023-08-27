package ru.practicum.shareit.user.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

//    private final ObjectMapper mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();
//
//    @Autowired
//    private MockMvc mockMvc;
//    @MockBean
//    private UserService userService;
//    private UserDto userDto;
//
//    @BeforeEach
//    void setUp() {
//        userDto = new UserDto();
//    }
//
//    @Test
//    @DisplayName("Получение всех пользователей")
//    void getAllUsers_whenInvoked_thenResponseStatusOkWithUsersCollectionsInBody() throws Exception {
//        List<UserDto> users = List.of(userDto);
//        when(userService.getAllUsers()).thenReturn(users);
//
//        String result = mockMvc.perform(get("/users")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(users), equalTo(result));
//        verify(userService, times(1)).getAllUsers();
//    }
//
//    @Test
//    @DisplayName("Сохранение пользователя")
//    void saveUser_whenInvoked_thenResponseStatusOk() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("mail@mail.ru");
//        userDto.setName("Alina");
//
//        when(userService.saveUser(any(UserDto.class))).thenReturn(userDto);
//
//        String result = mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(userDto), equalTo(result));
//    }
//
//    @Test
//    void saveUser_whenEmailIsNull_thenResponseStatusBadRequest() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail(null);
//        userDto.setName("Alina");
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void saveUser_whenEmailIsBlank_thenResponseStatusBadRequest() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("");
//        userDto.setName("Alina");
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void saveUser_whenEmailIsNotValid_thenResponseStatusBadRequest() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("alina");
//        userDto.setName("Alina");
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//    @Test
//    void saveUser_whenNameIsNull_thenResponseStatusBadRequest() throws Exception {
//        UserDto userDto = new UserDto();
//        userDto.setEmail("alina@mail.ru");
//        userDto.setName(null);
//
//        mockMvc.perform(post("/users")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isBadRequest());
//    }
//
//
//    @Test
//    @DisplayName("Получение пользователя")
//    void getById_whenInvoked_thenResponseStatusOk() throws Exception {
//        Mockito.when(userService.getById(1l)).thenReturn(userDto);
//
//        String result = mockMvc.perform(get("/users/1")
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse()
//                .getContentAsString(StandardCharsets.UTF_8);
//
//        MatcherAssert.assertThat(mapper.writeValueAsString(userDto), equalTo(result));
//        verify(userService, times(1)).getById(anyLong());
//    }
//
//    @Test
//    @DisplayName("Обновление пользователя")
//    void updateUser_whenInvoked() throws Exception {
//
//        Mockito.when(userService.updateUser(1l, userDto)).thenReturn(userDto);
//
//        mockMvc.perform(patch("/users/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(mapper.writeValueAsString(userDto)))
//                .andExpect(status().isOk());
//    }
}