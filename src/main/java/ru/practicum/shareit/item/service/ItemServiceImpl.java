package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.enumeration.Status;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.CommentException;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
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
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    private final CommentRepository commentRepository;

    private final BookingRepository bookingRepository;

    private final ItemRequestRepository itemRequestRepository;


    @Override
    public ItemDto getById(Long id, Long userId) {
        userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(id);
        Booking nextBooking = null;
        Booking lastBooking = null;
        if (item.getOwner().getId().equals(userId)) {
            nextBooking = bookingRepository.findFirstByItem_IdAndStatusAndStartIsAfterOrderByStartAsc(id, Status.APPROVED, LocalDateTime.now());
            lastBooking = bookingRepository.findFirstByItem_IdAndStatusAndStartIsBeforeOrderByEndDesc(id, Status.APPROVED, LocalDateTime.now());
        }
        List<Comment> comments = commentRepository.findAllByItemId(id);
        return ItemMapperImpl.toItemDaoResponse(item, lastBooking, nextBooking, comments);
    }

    @Override
    public List<ItemDto> getItemsByUser(Long userId, Integer from, Integer size) {
        userRepository.getUserById(userId);
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return itemRepository.findAllItemsByOwnerId(userId, pageable)
                .stream().map(item ->
                        getById(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item;
        if (itemDto.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.getItemRequestById(itemDto.getRequestId());
            item = ItemMapperImpl.toEntity(itemDto, user, itemRequest);
        } else item = ItemMapperImpl.toEntity(itemDto, user, null);
        return ItemMapperImpl.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.getItemById(itemId);
        userRepository.getUserById(userId);
        checkingOwnerTheItem(item, userId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null)
            item.setDescription(itemDto.getDescription());

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        return ItemMapperImpl.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public void deleteItem(Long itemId, Long userId) {
        Item item = itemRepository.getItemById(itemId);
        userRepository.getUserById(userId);
        checkingOwnerTheItem(item, userId);
        itemRepository.deleteById(itemId);
    }


    @Transactional
    @Override
    public CommentDto postComment(CommentDto commentDto, Long itemId, Long userId) {
        User user = userRepository.getUserById(userId);
        Item item = itemRepository.getItemById(itemId);
        List<Booking> bookings = bookingRepository
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(user, item, LocalDateTime.now(), Status.APPROVED);
        if (bookings.isEmpty()) {
            throw new CommentException("Пользователь не бронировал данный предмет");
        }

        Comment comment = CommentMapper.INSTANCE.toComment(commentDto, item, user);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.INSTANCE.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<ItemDto> getItemsSearch(String text, Long userId, Integer from, Integer size) {
        if (text.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        Pageable pageable = PageRequest.of(from, size, Sort.by(Sort.Direction.ASC, "id"));
        return itemRepository.findBySearch(text, pageable).stream()
                .map(ItemMapperImpl::toDto).collect(Collectors.toList());
    }

    private void checkingOwnerTheItem(Item item, Long userId) {
        if (!item.getOwner().getId().equals(userId)) {
            throw new ItemDoesNotBelongToUserException(String.format("Пользователь с id = " + userId +
                    " не является владельцем вещи: " + item));
        }
    }
}
