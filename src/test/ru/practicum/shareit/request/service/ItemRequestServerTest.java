package ru.practicum.shareit.request.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserIdValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemRequestServerTest {

    @InjectMocks
    private ItemRequestServerImpl itemRequestServer;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Mock
    private UserRepository userRepository;

    private ItemRequestDto itemRequestDto;
    private User user;

    @BeforeEach
    void setUp() {
        itemRequestDto = new ItemRequestDto();
        user = new User();
        user.setId(1l);
    }

    @Test
    void add() {
        ItemRequest expected = ItemRequestMapper.toEntity(itemRequestDto, user);
        when(itemRequestRepository.save(any(ItemRequest.class))).
                thenReturn(expected);
        when(userRepository.getUserById(user.getId())).thenReturn(user);

        ItemRequestDto actual = itemRequestServer.add(itemRequestDto, user.getId());

        assertThat(ItemRequestMapper.toDto(expected), equalTo(actual));
        verify(userRepository, times(1)).getUserById(anyLong());
    }

    @Test
    void findAllOwnerRequests() {
        Long userId = 0L;
        List<ItemRequest> expectedItemRequests = Arrays.asList(new ItemRequest(), new ItemRequest());
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.findAllByRequestor(any(User.class))).thenReturn(expectedItemRequests);

        List<ItemRequestDto> actualItemRequests = itemRequestServer.findAllOwnerRequests(userId);

        assertThat(expectedItemRequests.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList()),
                equalTo(actualItemRequests));
        InOrder inOrder = Mockito.inOrder(userRepository, itemRequestRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(itemRequestRepository, times(1)).findAllByRequestor(any(User.class));
    }

    @Test
    void findAllOwnerRequests_whenUserNotFount() {
        Long userId = 0L;
        when(userRepository.getUserById(anyLong())).thenThrow(UserIdValidationException.class);

        Exception exception = assertThrows(UserIdValidationException.class, () ->
            itemRequestServer.findAllOwnerRequests(userId)
        );

        assertThat(exception.getClass(), equalTo(UserIdValidationException.class));
        verify(userRepository, times(1)).getUserById(anyLong());
        verify(itemRequestRepository, times(0)).findAllByRequestor(any(User.class));
    }

    @Test
    void findAll() {
        List<ItemRequest> expectedItemRequests = Arrays.asList(new ItemRequest(), new ItemRequest());
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.findAllByRequestorIdNot(anyLong(), any(Pageable.class)))
                .thenReturn(expectedItemRequests);

        List<ItemRequestDto> actual = itemRequestServer.findAll(user.getId(), 0, 10);

        assertThat(expectedItemRequests.stream().map(ItemRequestMapper::toDto).collect(Collectors.toList()), equalTo(actual));
        InOrder inOrder = inOrder(userRepository, itemRequestRepository);
        inOrder.verify(userRepository, times(1)).getUserById(anyLong());
        inOrder.verify(itemRequestRepository, times(1)).findAllByRequestorIdNot(anyLong(), any(Pageable.class));
    }

    @Test
    void getRequestById() {
        ItemRequest itemRequest = new ItemRequest();
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getItemRequestById(anyLong())).thenReturn(itemRequest);

        ItemRequestDto actual = itemRequestServer.getRequestById(1l, 1l);

        assertThat(actual, equalTo(ItemRequestMapper.toDto(itemRequest)));
    }

    @Test
    void findAll_whenFromNotValid_thenExceptionThrown() {
        Long userId = 0L;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> itemRequestServer.findAll(userId, -1, 5));

        assertThat("Page index must not be less than zero",
                equalTo(exception.getMessage()));
        InOrder inOrder = inOrder(userRepository, itemRequestRepository);
        inOrder.verify(userRepository, never()).findById(anyLong());
        inOrder.verify(itemRequestRepository, never())
                .findAllByRequestorIdNot(anyLong(), any(Pageable.class));
    }

    @Test
    void findAll_whenSizeNotValid_thenExceptionThrown() {
        Long userId = 0L;

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> itemRequestServer.findAll(userId, 0, -5));

        assertThat("Page size must not be less than one",
                equalTo(exception.getMessage()));
        InOrder inOrder = inOrder(userRepository, itemRequestRepository);
        inOrder.verify(userRepository, never()).findById(anyLong());
        inOrder.verify(itemRequestRepository, never())
                .findAllByRequestorIdNot(anyLong(), any(Pageable.class));
    }
}