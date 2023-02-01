package ru.practicum.shareit.request;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        List<ItemRequestDto.Item> items = Optional.ofNullable(itemRequest.getItems())
                .orElse(new ArrayList<>())
                .stream()
                .map(ItemRequestMapper::toInnerItem)
                .collect(Collectors.toList());
        return ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .items(items)
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return ItemRequest.builder()
                .description(itemRequestDto.getDescription())
                .build();
    }

    private static ItemRequestDto.Item toInnerItem(Item item) {
        return ItemRequestDto.Item.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(Optional.ofNullable(item.getItemRequest()).isPresent()
                        ? item.getItemRequest().getId()
                        : null)
                .build();
    }
}
