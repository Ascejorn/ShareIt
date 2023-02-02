package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class ItemRequestMapperTest {

    private Item item;

    private User owner;

    private ItemRequest request;

    private User requester;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build();
        requester = User.builder()
                .name("Requester")
                .email("requester@mail.com")
                .build();
        request = ItemRequest
                .builder()
                .id(3L)
                .description("text")
                .requester(requester)
                .created(LocalDateTime.now())
                .build();
        item = Item.builder()
                .id(1L)
                .name("name")
                .description("description")
                .owner(owner)
                .available(true)
                .itemRequest(request)
                .comments(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("Item to InnerItem mapping")
    void toInnerItem() {
        ItemRequestDto.Item itemInner = ItemRequestMapper.toInnerItem(item);
        assertThat(itemInner).isNotNull();
        assertThat(itemInner.getId()).isEqualTo(item.getId());
        assertThat(itemInner.getName()).isEqualTo(item.getName());
        assertThat(itemInner.getDescription()).isEqualTo(item.getDescription());
        assertThat(itemInner.getRequestId()).isEqualTo(item.getItemRequest().getId());
    }

}