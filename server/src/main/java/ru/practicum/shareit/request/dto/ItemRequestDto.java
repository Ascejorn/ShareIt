package ru.practicum.shareit.request.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemRequestDto {
    Long id;

    String description;

    List<ItemRequestDto.Item> items;

    LocalDateTime created;

    @Getter
    @Setter
    @Builder
    public static class Item {

        private Long id;


        private String name;


        private String description;


        private Boolean available;

        private Long requestId;
    }
}
