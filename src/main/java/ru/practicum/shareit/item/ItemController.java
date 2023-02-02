package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemAdvancedDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {
    private final ItemService itemService;
    private final CommentService commentService;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ItemDto createItem(
            @Valid @RequestBody ItemDto itemDto,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("POST /items {}", itemDto);
        return itemService.create(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDto updateItem(
            @RequestBody ItemDto itemDto,
            @PathVariable Long itemId,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("PATCH /items/{} {}", itemId, itemDto);
        return itemService.update(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    public ItemAdvancedDto getItemById(
            @PathVariable Long itemId,
            @RequestHeader(USER_ID_HEADER) Long userId
    ) {
        log.info("GET /items/{}", itemId);
        return itemService.getById(itemId, userId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemAdvancedDto> getItemsByOwnerId(
            @RequestHeader(USER_ID_HEADER) Long ownerId,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        log.info("GET /items");
        return itemService.getAllByOwnerId(from, size, ownerId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ItemDto> searchItems(
            @RequestParam(name = "text", defaultValue = "") String text,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        log.info("GET /items/search?text={}", text);
        return itemService.findAvailableByNameOrDescription(from, size, text);
    }

    @PostMapping("/{itemId}/comment")
    @ResponseStatus(HttpStatus.OK)
    public CommentDto createComment(
            @Valid @RequestBody CommentDto commentDto,
            @PathVariable Long itemId,
            @RequestHeader(USER_ID_HEADER) Long authorId
    ) {
        log.info("POST /items/{}/comment '{}'", itemId, commentDto.getText());
        return commentService.create(commentDto, itemId, authorId);
    }

}
