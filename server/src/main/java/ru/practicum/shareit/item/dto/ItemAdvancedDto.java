package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.dto.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemAdvancedDto {
    Long id;

    String name;

    String description;

    Boolean available;

    ItemAdvancedDto.Booking lastBooking;

    ItemAdvancedDto.Booking nextBooking;

    List<ItemAdvancedDto.Comment> comments;

    Long requestId;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class Booking {
        private Long id;
        private BookingStatus status;
        private LocalDateTime start;
        private LocalDateTime end;
        private Long itemId;
        private Long bookerId;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Comment {
        private Long id;
        private String authorName;
        private String text;
        private LocalDateTime created;
    }
}
