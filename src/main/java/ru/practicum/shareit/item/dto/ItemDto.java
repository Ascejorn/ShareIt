package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;

import javax.validation.constraints.Positive;

@Data
@Builder
public class ItemDto {

    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotNull(message = "Availability is required.")
    private Boolean available;

    @Positive(message = "Request should be positive.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;
}
