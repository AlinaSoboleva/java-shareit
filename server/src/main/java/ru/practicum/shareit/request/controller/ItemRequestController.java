package ru.practicum.shareit.request.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestServer;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

import static ru.practicum.shareit.util.RequestHeaders.X_SHARER_USER_ID;

@Controller
@RequestMapping(path = "/requests")
@AllArgsConstructor
public class ItemRequestController {

    private final ItemRequestServer itemRequestServer;

    @PostMapping
    ResponseEntity<ItemRequestDto> add(@RequestBody ItemRequestDto itemRequestDto,
                                       @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return ResponseEntity.ok(itemRequestServer.add(itemRequestDto, userId));
    }

    @GetMapping
    ResponseEntity<List<ItemRequestDto>> findAllOwnerRequests(@RequestHeader(X_SHARER_USER_ID) Long userId) {
        return ResponseEntity.ok(itemRequestServer.findAllOwnerRequests(userId));
    }

    @GetMapping("/all")
    @Validated
    ResponseEntity<List<ItemRequestDto>> findAll(@RequestHeader(X_SHARER_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                 @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size) {
        return ResponseEntity.ok(itemRequestServer.findAll(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequestById(
            @PathVariable Long requestId,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return ResponseEntity.ok(itemRequestServer.getRequestById(requestId, userId));
    }
}
