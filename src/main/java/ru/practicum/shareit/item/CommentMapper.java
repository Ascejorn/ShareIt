package ru.practicum.shareit.item;


import lombok.experimental.UtilityClass;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemAdvancedDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class CommentMapper {
    public static CommentDto toCommentDto(Comment comment) {
        return CommentDto.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }

    public List<ItemAdvancedDto.Comment> toCommentDtoList(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::toInnerComment)
                .collect(Collectors.toList());
    }

    public static Comment toComment(CommentDto commentDto, User author, Item item) {
        return Comment.builder()
                .author(author)
                .text(commentDto.getText())
                .item(item)
                .build();
    }

    protected static ItemAdvancedDto.Comment toInnerComment(Comment comment) {
        return ItemAdvancedDto.Comment.builder()
                .id(comment.getId())
                .authorName(comment.getAuthor().getName())
                .text(comment.getText())
                .created(comment.getCreated())
                .build();
    }
}
