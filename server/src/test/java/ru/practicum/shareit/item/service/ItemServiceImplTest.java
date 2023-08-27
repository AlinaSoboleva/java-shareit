package ru.practicum.shareit.item.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.CommentException;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @Captor
    private ArgumentCaptor<Item> itemCaptor;

    private final long itemId = 0L;
    private final long userId = 0l;


    @Test
    void getById() {
        Item expectedItem = new Item();
        User user = new User();
        user.setId(1L);
        expectedItem.setOwner(user);
        when(itemRepository.getItemById(itemId)).thenReturn(expectedItem);
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(Collections.EMPTY_LIST);

        ItemDto actualItem = itemService.getById(itemId, userId);

        InOrder inOrder = inOrder(itemRepository, commentRepository);
        inOrder.verify(itemRepository, times(1)).getItemById(itemId);
        inOrder.verify(commentRepository, times(1)).findAllByItemId(itemId);
        assertThat(ItemMapperImpl.toItemDaoResponse(expectedItem, null, null, Collections.EMPTY_LIST), equalTo(actualItem));
    }

    @Test
    void getById_whenItemNotFound() {
        when(itemRepository.getItemById(itemId)).thenThrow(ItemIdValidationException.class);

        Exception exception = assertThrows(ItemIdValidationException.class,
                () -> itemService.getById(itemId, 0L));

        assertThat(ItemIdValidationException.class, equalTo(exception.getClass()));
        verify(itemRepository, times(1)).getItemById(itemId);
    }

    @Test
    void getById_withBookings() {
        User user = new User();
        user.setId(userId);
        Item expectedItem = new Item();
        expectedItem.setId(itemId);
        expectedItem.setOwner(user);

        Booking lastBooking = new Booking();
        lastBooking.setId(5L);
        lastBooking.setStart(LocalDateTime.now());
        lastBooking.setEnd(LocalDateTime.now().plusHours(1));
        lastBooking.setStatus(Status.APPROVED);
        lastBooking.setItem(expectedItem);

        Booking nextBooking = new Booking();
        nextBooking.setId(7L);
        nextBooking.setStart(LocalDateTime.now());
        nextBooking.setEnd(LocalDateTime.now().plusHours(2));
        nextBooking.setStatus(Status.APPROVED);
        nextBooking.setItem(expectedItem);

        Comment comment = new Comment();
        comment.setId(2L);
        comment.setText("text");
        comment.setCreated(LocalDateTime.now());
        comment.setAuthor(user);
        comment.setItem(expectedItem);

        when(itemRepository.getItemById(itemId)).thenReturn(expectedItem);
        when(bookingRepository.findFirstByItem_IdAndStatusAndStartIsBeforeOrderByEndDesc(anyLong(), any(Status.class),
                any(LocalDateTime.class))).thenReturn(lastBooking);
        when(bookingRepository.findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(anyLong(), any(Status.class),
                any(LocalDateTime.class))).thenReturn(nextBooking);
        when(commentRepository.findAllByItemId(anyLong())).thenReturn(List.of(comment));

        ItemDto actualItem = itemService.getById(itemId, userId);

        assertThat(ItemMapperImpl.toItemDaoResponse(expectedItem,
                lastBooking, nextBooking, Arrays.asList(comment)), equalTo(actualItem));
        InOrder inOrder = inOrder(itemRepository, bookingRepository, commentRepository);
        inOrder.verify(itemRepository, times(1)).getItemById(itemId);
        inOrder.verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(anyLong(), any(Status.class),
                        any(LocalDateTime.class));
        inOrder.verify(bookingRepository, times(1))
                .findFirstByItem_IdAndStatusAndStartIsBeforeOrderByEndDesc(anyLong(), any(Status.class),
                        any(LocalDateTime.class));
        inOrder.verify(commentRepository, times(1)).findAllByItemId(itemId);
    }

    @Test
    void getItemsByUser() {
        User user = new User();
        user.setId(userId);
        Item item = new Item();
        item.setId(itemId);
        item.setOwner(user);
        List<Item> expectedItems = List.of(item);

        when(itemRepository.findAllItemsByOwnerId(anyLong(), any(Pageable.class))).thenReturn(expectedItems);
        when(itemRepository.getItemById(anyLong())).thenReturn(item);

        List<ItemDto> actualItems = itemService.getItemsByUser(userId, 0, 1);

        assertThat(expectedItems.size(), equalTo(actualItems.size()));
        assertThat(expectedItems.get(0).getId(), equalTo(actualItems.get(0).getId()));
        assertThat(expectedItems.get(0).getName(), equalTo(actualItems.get(0).getName()));
        assertThat(expectedItems.get(0).getDescription(), equalTo(actualItems.get(0).getDescription()));
        assertThat(expectedItems.get(0).getAvailable(), equalTo(actualItems.get(0).getAvailable()));
        assertThat(expectedItems.get(0).getRequest(), equalTo(actualItems.get(0).getRequestId()));

        InOrder inOrder = inOrder(itemRepository, commentRepository);
        inOrder.verify(itemRepository, times(1))
                .findAllItemsByOwnerId(anyLong(), any(Pageable.class));
        inOrder.verify(itemRepository, times(1)).getItemById(anyLong());
        inOrder.verify(commentRepository, times(1)).findAllByItemId(anyLong());
    }

    @Test
    void saveItem() {
        ItemDto itemToSave = new ItemDto();
        itemToSave.setAvailable(true);
        User user = new User();
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRepository.save(any(Item.class)))
                .thenReturn(ItemMapperImpl.toEntity(itemToSave, user, null));

        ItemDto actualItem = itemService.saveItem(itemToSave, userId);

        assertThat(itemToSave, equalTo(actualItem));
        InOrder inOrder = inOrder(userRepository, itemRepository);
        inOrder.verify(userRepository, times(1)).getUserById(userId);
        inOrder.verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void saveItem_withRequest() {
        User user = new User();
        ItemDto itemToSave = new ItemDto();
        itemToSave.setAvailable(true);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(1L);

        itemToSave.setRequestId(1L);

        Item item = ItemMapperImpl.toEntity(itemToSave, user, itemRequest);
        item.setRequest(itemRequest);

        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(itemRequestRepository.getItemRequestById(anyLong())).thenReturn(itemRequest);
        when(itemRepository.save(any(Item.class)))
                .thenReturn(item);

        ItemDto actualItem = itemService.saveItem(itemToSave, userId);

        assertThat(itemToSave, equalTo(actualItem));
        InOrder inOrder = inOrder(userRepository, itemRequestRepository, itemRepository);
        inOrder.verify(userRepository, times(1)).getUserById(userId);
        inOrder.verify(itemRequestRepository, times(1)).getItemRequestById(1L);
        inOrder.verify(itemRepository, times(1)).save(any(Item.class));
    }


    @Test
    void updateItem() {
        User user = new User();
        user.setId(userId);

        Item oldItem = new Item();
        oldItem.setName("oldName");
        oldItem.setDescription("oldDesc");
        oldItem.setAvailable(false);
        oldItem.setOwner(user);

        when(itemRepository.getItemById(anyLong())).thenReturn(oldItem);
        when(userRepository.getUserById(anyLong())).thenReturn(new User());

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(5L);

        Item newItem = new Item();
        newItem.setName("newName");
        newItem.setDescription("newDesc");
        newItem.setAvailable(true);
        newItem.setRequest(itemRequest);

        itemService.updateItem(ItemMapperImpl.toDto(newItem), itemId, userId);
        verify(itemRepository).save(itemCaptor.capture());
        Item savedItem = itemCaptor.getValue();

        assertThat(newItem.getName(), equalTo(savedItem.getName()));
        assertThat(newItem.getDescription(), equalTo(savedItem.getDescription()));
        assertThat(newItem.getAvailable(), equalTo(savedItem.getAvailable()));

        InOrder inOrder = inOrder(userRepository, itemRepository);
        verify(itemRepository, times(1)).getItemById(itemId);
        inOrder.verify(userRepository, times(1)).getUserById(userId);
        inOrder.verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_whereItemDoesNotBelongToUser() {
        User user = new User();
        user.setId(userId);

        User anotherUser = new User();
        user.setId(1l);

        Item oldItem = new Item();
        oldItem.setName("oldName");
        oldItem.setDescription("oldDesc");
        oldItem.setAvailable(false);
        oldItem.setOwner(user);

        when(itemRepository.getItemById(anyLong())).thenReturn(oldItem);
        when(userRepository.getUserById(anyLong())).thenReturn(anotherUser);

        Item newItem = new Item();
        newItem.setName("newName");
        newItem.setDescription("newDesc");
        newItem.setAvailable(true);

        ItemDto itemDto = ItemMapperImpl.toDto(newItem);

        Exception exception = assertThrows(ItemDoesNotBelongToUserException.class, () ->
                itemService.updateItem(itemDto, itemId, userId));


        assertThat("Пользователь с id = " + userId +
                " не является владельцем вещи: " + oldItem, equalTo(exception.getMessage()));
        assertThat(ItemDoesNotBelongToUserException.class, equalTo(exception.getClass()));

        InOrder inOrder = inOrder(itemRepository, userRepository);
        verify(itemRepository, times(1)).getItemById(itemId);
        inOrder.verify(userRepository, times(1)).getUserById(userId);
    }

    @Test
    void postComment() {
        Comment comment = new Comment();
        CommentDto commentToSave = CommentMapper.INSTANCE.toCommentDto(comment);

        User user = new User();

        Item item = new Item();

        when(itemRepository.getItemById(anyLong())).thenReturn(item);
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingsByBookerAndItemAndEndBeforeAndStatus(any(User.class), any(Item.class), any(LocalDateTime.class), any(Status.class)))
                .thenReturn(List.of(new Booking()));
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        CommentDto actualComment = itemService.postComment(commentToSave, itemId, userId);

        assertThat(commentToSave, equalTo(actualComment));
        InOrder inOrder = inOrder(userRepository, itemRepository, bookingRepository, commentRepository);
        inOrder.verify(userRepository, times(1)).getUserById(itemId);
        inOrder.verify(itemRepository, times(1)).getItemById(userId);
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(any(User.class), any(Item.class), any(LocalDateTime.class), any(Status.class));
        inOrder.verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void postComment_ifBookingIsEmpty() {
        Comment comment = new Comment();
        CommentDto commentToSave = CommentMapper.INSTANCE.toCommentDto(comment);

        User user = new User();

        Item item = new Item();

        when(itemRepository.getItemById(anyLong())).thenReturn(item);
        when(userRepository.getUserById(anyLong())).thenReturn(user);
        when(bookingRepository.findBookingsByBookerAndItemAndEndBeforeAndStatus(any(User.class), any(Item.class), any(LocalDateTime.class), any(Status.class)))
                .thenReturn(Collections.EMPTY_LIST);


        Exception exception = assertThrows(CommentException.class, () ->
                itemService.postComment(commentToSave, itemId, userId));

        assertThat("Пользователь не бронировал данный предмет", equalTo(exception.getMessage()));
        assertThat(CommentException.class, equalTo(exception.getClass()));
        InOrder inOrder = inOrder(userRepository, itemRepository, bookingRepository);
        inOrder.verify(userRepository, times(1)).getUserById(itemId);
        inOrder.verify(itemRepository, times(1)).getItemById(userId);
        inOrder.verify(bookingRepository, times(1))
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(any(User.class), any(Item.class), any(LocalDateTime.class), any(Status.class));
    }

    @Test
    void getItemsSearch_ifTextIsNotEmpty() {
        List<Item> expectedItems = Arrays.asList(new Item(), new Item());
        when(itemRepository.findBySearch(anyString(), any(Pageable.class))).thenReturn(expectedItems);

        List<ItemDto> actualItems = itemService.getItemsSearch("text", userId, 0, 1);

        assertThat(expectedItems.stream().map(ItemMapperImpl::toDto).collect(Collectors.toList()),
                equalTo(actualItems));
        verify(itemRepository, times(1))
                .findBySearch("text", PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id")));
    }

    @Test
    void getItemsSearch_ifTextIsEmpty() {
        List<ItemDto> actualItems = itemService.getItemsSearch("", userId, 0, 1);

        assertThat(actualItems, empty());
        verify(itemRepository, never()).findBySearch("", PageRequest.of(0, 1));
    }
}