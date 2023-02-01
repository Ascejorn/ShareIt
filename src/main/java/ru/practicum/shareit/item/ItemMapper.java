package ru.practicum.shareit.item;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.ItemAdvancedDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@UtilityClass
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                        ? item.getItemRequest().getId()
                        : null)
                .build();
    }

    public static ItemAdvancedDto toItemAdvancedDto(
            Item item, BookingDto lastBooking, BookingDto nextBooking
    ) {
        if (lastBooking == null && nextBooking == null) {
            return ItemAdvancedDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(null)
                    .comments(CommentMapper.toCommentDtoList(item.getComments()))
                    .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                            ? item.getItemRequest().getId()
                            : null)
                    .build();

        } else if (lastBooking == null) {
            return ItemAdvancedDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(null)
                    .nextBooking(new ItemAdvancedDto.Booking(nextBooking.getId(),
                            nextBooking.getStatus(),
                            nextBooking.getStart(),
                            nextBooking.getEnd(),
                            nextBooking.getItemId(),
                            nextBooking.getBookerId()))
                    .comments(CommentMapper.toCommentDtoList(item.getComments()))
                    .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                            ? item.getItemRequest().getId()
                            : null)
                    .build();

        } else if (nextBooking == null) {
            return ItemAdvancedDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(new ItemAdvancedDto.Booking(lastBooking.getId(),
                            lastBooking.getStatus(),
                            lastBooking.getStart(),
                            lastBooking.getEnd(),
                            lastBooking.getItemId(),
                            lastBooking.getBookerId()))
                    .nextBooking(null)
                    .comments(CommentMapper.toCommentDtoList(item.getComments()))
                    .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                            ? item.getItemRequest().getId()
                            : null)
                    .build();

        } else {
            return ItemAdvancedDto.builder()
                    .id(item.getId())
                    .name(item.getName())
                    .description(item.getDescription())
                    .available(item.getAvailable())
                    .lastBooking(new ItemAdvancedDto.Booking(lastBooking.getId(),
                            lastBooking.getStatus(),
                            lastBooking.getStart(),
                            lastBooking.getEnd(),
                            lastBooking.getItemId(),
                            lastBooking.getBookerId()))
                    .nextBooking(new ItemAdvancedDto.Booking(nextBooking.getId(),
                            nextBooking.getStatus(),
                            nextBooking.getStart(),
                            nextBooking.getEnd(),
                            nextBooking.getItemId(),
                            nextBooking.getBookerId()))
                    .comments(CommentMapper.toCommentDtoList(item.getComments()))
                    .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                            ? item.getItemRequest().getId()
                            : null)
                    .build();
        }
    }

    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .build();
    }
}
