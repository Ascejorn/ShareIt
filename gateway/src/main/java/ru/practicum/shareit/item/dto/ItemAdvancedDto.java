package ru.practicum.shareit.item.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;


@Getter
public class ItemAdvancedDto {
    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotNull(message = "Availability is required.")
    private Boolean available;

    @Positive(message = "Request should be positive.")
    private Long requestId;
}
