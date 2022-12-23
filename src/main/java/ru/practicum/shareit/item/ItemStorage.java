package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;

public interface ItemStorage {
    Item add(Item item);

    Optional<Item> getById(Long itemId);

    Collection<Item> getAll();
}
