package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    ResponseEntity<Object> add(@RequestBody @Valid ItemRequestDto itemRequestDto,
                               @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.add(itemRequestDto, userId);
    }


    @GetMapping
    ResponseEntity<Object> findAllOwnerRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemRequestClient.findAllOwnerRequests(userId);
    }

    @GetMapping("/all")
    @Validated
    ResponseEntity<Object> findAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                   @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return itemRequestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @PathVariable Long requestId) {
        return itemRequestClient.getRequestById(requestId, userId);
    }
}