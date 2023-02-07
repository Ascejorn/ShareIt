package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {
    @NotBlank(message = "Name is required.")
    String name;

    @NotBlank(message = "Description is required.")
    String description;

    @NotNull(message = "Availability is required.")
    Boolean available;

    @Positive(message = "Request should be positive.")
    Long requestId;
}
