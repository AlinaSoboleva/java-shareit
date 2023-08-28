package ru.practicum.shareit.request.exception;

public class RequestIdValidationException extends RuntimeException {
    public RequestIdValidationException(String message) {
        super(message);
    }
}
