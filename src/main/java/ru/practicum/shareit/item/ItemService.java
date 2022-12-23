package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    public ItemDto create(ItemDto itemDto, Long userId) {
        Item item = ItemMapper.toItem(itemDto);
        Optional<User> ownerOptional = userStorage.getById(userId);
        if (ownerOptional.isEmpty()) {
            throw new NotFoundException("User not found.");
        }
        item.setOwner(ownerOptional.get());
        return ItemMapper.toItemDto(itemStorage.add(item));
    }

    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        Item item = getByIdWithExceptionCheck(itemId);
        if (!item.getOwner().getId().equals(userId)) {
            throw new NotFoundException("Item belongs to other user.");
        }
        Optional<String> name = Optional.ofNullable(itemDto.getName());
        if (name.isPresent()) {
            if (!name.get().isBlank()) {
                item.setName(name.get());
            }
        }
        Optional<String> description = Optional.ofNullable(itemDto.getDescription());
        if (description.isPresent()) {
            if (!description.get().isBlank()) {
                item.setDescription(description.get());
            }
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        itemStorage.add(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto getById(Long itemId) {
        return ItemMapper.toItemDto(getByIdWithExceptionCheck(itemId));
    }

    public List<ItemDto> getOwnerById(Long ownerId) {
        return itemStorage.getAll().stream()
                .filter(i -> Objects.equals(i.getOwner().getId(), ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    public List<ItemDto> getAvailableByNameOrDescription(String text) {
        if (text.isBlank()) return new ArrayList<>();
        return itemStorage.getAll().stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(i -> i.getAvailable().equals(Boolean.TRUE))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private Item getByIdWithExceptionCheck(Long itemId) {
        Optional<Item> itemOptional = itemStorage.getById(itemId);
        if (itemOptional.isEmpty()) {
            throw new NotFoundException("Item by id " + itemId);
        }
        return itemOptional.get();
    }
}
