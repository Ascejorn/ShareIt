package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.validation.exception.BadRequestException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final BookingService bookingService;
    private final CommentRepository commentRepository;

    @Override
    public CommentDto create(CommentDto commentDto, Long itemId, Long authorId) {
        if (commentDto.getText().isBlank()) {
            throw new BadRequestException("Empty comment.");
        }
        Booking booking = bookingService.findApprovedOrNotAvailableError(authorId, itemId);
        if (booking.getStart().isAfter(LocalDateTime.now())) {
            throw new BadRequestException("Booking not completed.");
        }
        Comment comment = commentRepository.save(
                CommentMapper.toComment(commentDto, booking.getBooker(), booking.getItem())
        );
        booking.getItem().addComment(comment);
        return CommentMapper.toCommentDto(comment);
    }
}
