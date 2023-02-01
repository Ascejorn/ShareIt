package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.Positive;

import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemAdvancedDto {

    private Long id;

    @NotBlank(message = "Name is required.")
    private String name;

    @NotBlank(message = "Description is required.")
    private String description;

    @NotNull(message = "Availability is required.")
    private Boolean available;

    private ItemAdvancedDto.Booking lastBooking;

    private ItemAdvancedDto.Booking nextBooking;

    private List<ItemAdvancedDto.Comment> comments;

    @Positive(message = "Request should be positive.")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Booking {
        private Long id;
        private BookingStatus status;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long itemId;
        private Long bookerId;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Comment {
        private Long id;
        private String authorName;
        private String text;
        private LocalDateTime created;
    }
}
