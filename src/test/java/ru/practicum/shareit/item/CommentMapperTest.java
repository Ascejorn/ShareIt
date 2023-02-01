package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemAdvancedDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

class CommentMapperTest {
    Item item;

    User owner;

    User author;

    ItemRequest request;

    User requester;

    Comment comment;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .name("Owner")
                .email("owner@mail.com")
                .build();
        author = User.builder()
                .name("Author")
                .email("author@mail.com")
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
        comment = Comment.builder()
                .id(1L)
                .item(item)
                .author(author)
                .text("text")
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    @DisplayName("Comment to InnerComment mapping")
    void toInnerComment() {
        ItemAdvancedDto.Comment commentInner = CommentMapper.toInnerComment(comment);
        assertThat(commentInner).isNotNull();
        assertThat(commentInner.getId()).isEqualTo(comment.getId());
        assertThat(commentInner.getAuthorName()).isEqualTo(comment.getAuthor().getName());
        assertThat(commentInner.getText()).isEqualTo(comment.getText());
        assertThat(commentInner.getCreated()).isEqualTo(comment.getCreated());
    }

}