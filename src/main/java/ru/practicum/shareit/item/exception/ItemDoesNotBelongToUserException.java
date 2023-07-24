package ru.practicum.shareit.item.exception;

public class ItemDoesNotBelongToUserException extends RuntimeException {
    public ItemDoesNotBelongToUserException(String message) {
        super(message);
    }
}
