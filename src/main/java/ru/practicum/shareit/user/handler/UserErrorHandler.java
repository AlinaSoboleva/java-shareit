package ru.practicum.shareit.user.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.response.ErrorResponse;
import ru.practicum.shareit.user.exception.UserEmailValidationException;
import ru.practicum.shareit.user.exception.UserIdValidationException;

@RestControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailException(final UserEmailValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleIdException(final UserIdValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

}
