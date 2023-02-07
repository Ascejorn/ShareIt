package ru.practicum.shareit.booking.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingAdvancedDto {

    Long id;

    BookingStatus status;

    LocalDateTime start;

    LocalDateTime end;

    BookingAdvancedDto.User booker;

    BookingAdvancedDto.Item item;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {
        private Long id;
        private String name;
        private String email;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private Long id;
        private String name;
        private Boolean available;
    }
}
