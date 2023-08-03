package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.dto.BookingDtoRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BookingDateValidator implements ConstraintValidator<BookingDateValid, BookingDtoRequest> {
    @Override
    public boolean isValid(BookingDtoRequest bookingDtoRequest, ConstraintValidatorContext context) {
        return bookingDtoRequest.getStart() == null ||
                bookingDtoRequest.getEnd() == null ||
                (!bookingDtoRequest.getStart().isAfter(bookingDtoRequest.getEnd()) &&
                        !bookingDtoRequest.getStart().isEqual(bookingDtoRequest.getEnd()));
    }
}
