package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private static final String HEADER_OF_OWNER = "X-Sharer-User-Id";
    private final ItemService itemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto create(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader(HEADER_OF_OWNER) Long userId
    ) {
        log.info("POST /items :: {} :: {}", userId, itemDto);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(HEADER_OF_OWNER) Long userId
    ) {
        log.info("PATCH /items/{} :: {} :: {}", itemId, userId, itemDto);
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemDto getById(@PathVariable Long itemId) {
        log.info("GET /items/{}", itemId);
        return itemService.getById(itemId);
    }

    @GetMapping
    public List<ItemDto> getByOwnerId(
            @RequestHeader(HEADER_OF_OWNER) Long ownerId
    ) {
        log.info("GET /items");
        log.info("GET /items :: {}", ownerId);
        return itemService.getOwnerById(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> search(
            @RequestParam(name = "text", defaultValue = "") String text
    ) {
        log.info("GET /items/search :: {}", text);
        return itemService.getAvailableByNameOrDescription(text);
    }
}