package ru.practicum.shareit.item.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.response.ErrorResponse;

@RestControllerAdvice
public class ItemErrorHandler {

    @ExceptionHandler(value = {ItemIdValidationException.class, ItemDoesNotBelongToUserException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIdException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
