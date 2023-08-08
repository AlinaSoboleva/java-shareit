package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
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
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapperImpl;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exception.UserIdValidationException;
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


    @Override
    public ItemDto getById(Long id, Long userId) {
        userIdValidate(userId);
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new ItemIdValidationException(String.format("Предмет с id: %s не найдена", id)));
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
    public List<ItemDto> getItemsByUser(Long userId) {
        userIdValidate(userId);
        return itemRepository.findAllItemsByOwnerId(userId, Sort.by(Sort.Direction.ASC, "id"))
                .stream().map(item ->
                        getById(item.getId(), userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ItemDto saveItem(ItemDto itemDto, Long userId) {
        User user = userIdValidate(userId);
        Item item = ItemMapperImpl.toEntity(itemDto, user);
        return ItemMapperImpl.toDto(itemRepository.save(item));
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemIdValidationException(String.format("Предмет с id: %s не найдена", itemId)));
        userIdValidate(userId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new ItemDoesNotBelongToUserException(String.format("Пользователь с id = " + userId +
                    " не является владельцем вещи: " + itemDto));
        }

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
        itemRepository.deleteById(itemId);
    }

    private User userIdValidate(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new UserIdValidationException(String.format("Пользователь с id: %s не найден", userId)));
    }

    @Transactional
    @Override
    public CommentDto postComment(CommentDto commentDto, Long itemId, Long userId) {
        User user = userIdValidate(userId);
        Item item = itemRepository.findById(itemId).orElseThrow(() ->
                new ItemIdValidationException(String.format("Предмет с id: %s не найдена", itemId)));
        List<Booking> bookings = bookingRepository
                .findBookingsByBookerAndItemAndEndBeforeAndStatus(user, item, LocalDateTime.now(), Status.APPROVED);
        if (bookings.isEmpty()) {
            throw new CommentException("Пользователь не бронировал данный предмет");
        }
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.INSTANCE.toComment(commentDto, item, user);

        return CommentMapper.INSTANCE.toCommentDto(commentRepository.save(comment));
    }

    @Override
    public List<ItemDto> getItemsSearch(String text, Long userId) {
        if (text.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        return itemRepository.findBySearch(text).stream()
                .map(ItemMapperImpl::toDto).collect(Collectors.toList());
    }
}
