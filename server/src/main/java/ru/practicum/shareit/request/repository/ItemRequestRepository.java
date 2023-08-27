package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.exception.RequestIdValidationException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findAllByRequestor(User user);

    List<ItemRequest> findAllByRequestorIdNot(Long requestorId, Pageable page);

    default ItemRequest getItemRequestById(Long requestId) {
        return findById(requestId).orElseThrow(() ->
                new RequestIdValidationException(String.format("Запрос с id: %s не найден ", requestId)));
    }
}
