package ru.practicum.shareit.request.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ItemRequestDto {

    private Long id;

    @NotNull(message = "Description are required.")
    private String description;

    private List<ItemRequestDto.Item> items;

    private LocalDateTime created;

    @Data
    @Builder
    public static class Item {

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
}
