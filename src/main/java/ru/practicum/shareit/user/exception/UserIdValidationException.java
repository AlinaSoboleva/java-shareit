package ru.practicum.shareit.user.exception;

public class UserIdValidationException extends RuntimeException {
    public UserIdValidationException(String message) {
        super(message);
    }
}
