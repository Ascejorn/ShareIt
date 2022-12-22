package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.item.model.Item;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items;
    private long idGenerator;

    @Override
    public Item add(Item item) {
        if (item.getId() == null) {
            item.setId(++idGenerator);
            items.put(idGenerator, item);
        } else {
            items.put(item.getId(), item);
        }
        return item;
    }

    @Override
    public Optional<Item> getById(Long itemId) {
        return (items.containsKey(itemId))
                ? Optional.of(items.get(itemId))
                : Optional.empty();
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }
}
