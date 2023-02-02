package ru.practicum.shareit.booking.dto;


import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class BookingAdvancedDto {

    private Long id;

    private BookingStatus status;

    private LocalDateTime start;

    private LocalDateTime end;

    private BookingAdvancedDto.User booker;

    private BookingAdvancedDto.Item item;

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
