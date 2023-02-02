package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingAdvancedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

@UtilityClass
public class BookingMapper {
    public static Booking toBooking(
            BookingDto bookingDto,
            Item item, User booker,
            BookingStatus bookingStatus
    ) {
        return Booking.builder()
                .item(item)
                .booker(booker)
                .status(bookingStatus)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .build();
    }

    public static BookingAdvancedDto toBookingAdvancedDto(Booking booking) {
        return BookingAdvancedDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .item(new BookingAdvancedDto.Item(booking.getItem().getId(),
                        booking.getItem().getName(), booking.getItem().getAvailable()))
                .booker(new BookingAdvancedDto.User(booking.getBooker().getId(),
                        booking.getBooker().getName(), booking.getBooker().getEmail()))
                .build();
    }

    public static BookingDto toBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .status(booking.getStatus())
                .bookerId(booking.getBooker().getId())
                .itemId(booking.getItem().getId())
                .build();
    }
}
