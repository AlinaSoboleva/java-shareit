package ru.practicum.shareit;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingException;
import ru.practicum.shareit.booking.exception.BookingStateException;
import ru.practicum.shareit.booking.exception.BookingValidException;
import ru.practicum.shareit.item.exception.CommentException;
import ru.practicum.shareit.item.exception.ItemDoesNotBelongToUserException;
import ru.practicum.shareit.item.exception.ItemIdValidationException;
import ru.practicum.shareit.response.ErrorResponse;
import ru.practicum.shareit.user.exception.UserEmailValidationException;
import ru.practicum.shareit.user.exception.UserIdValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(value = {ItemIdValidationException.class, ItemDoesNotBelongToUserException.class, BookingValidException.class, UserIdValidationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemIdException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleEmailException(final UserEmailValidationException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIdException(final MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({BookingException.class, BookingStateException.class, CommentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingException(final RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleIdException(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }
}
