package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    @NotBlank(message = "Description are required.")
    String description;
}
