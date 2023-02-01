package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Getter;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.validation.EndLessThanStartValidation;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Builder
@EndLessThanStartValidation
public class BookingDto {

    private Long id;

    private BookingStatus status;

    @NotNull(message = "Start booking is required.")
    @FutureOrPresent(message = "Start cannot be in the past.")
    private LocalDateTime start;

    @NotNull(message = "End booking is required.")
    @FutureOrPresent(message = "End cannot be in the past.")
    private LocalDateTime end;

    @NotNull(message = "Item id is required.")
    private Long itemId;

    private Long bookerId;
}
