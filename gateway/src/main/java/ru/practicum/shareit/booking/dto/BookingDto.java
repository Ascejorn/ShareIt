package ru.practicum.shareit.booking.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.validation.EndLessThanStartValidation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@EndLessThanStartValidation
public class BookingDto {
    @NotNull(message = "Start booking is required.")
    @FutureOrPresent(message = "Start cannot be in the past.")
    LocalDateTime start;

    @NotNull(message = "End booking is required.")
    @FutureOrPresent(message = "End cannot be in the past.")
    LocalDateTime end;

    @NotNull(message = "Item id is required.")
    Long itemId;
}
