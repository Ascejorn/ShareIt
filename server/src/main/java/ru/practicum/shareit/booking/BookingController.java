package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingAdvancedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {
    private final BookingService bookingService;

    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    public static final String DEFAULT_STATE = "ALL";

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public BookingAdvancedDto create(
            @RequestBody BookingDto bookingDto,
            @RequestHeader(name = USER_ID_HEADER) Long bookerId
    ) {
        log.info("POST /bookings {}", bookingDto);
        return bookingService.create(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingAdvancedDto approve(
            @RequestHeader(name = USER_ID_HEADER) Long ownerId,
            @RequestParam boolean approved,
            @PathVariable Long bookingId
    ) {
        log.info("PATCH /bookings/{}?approved={}", bookingId, approved);
        return bookingService.approve(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    @ResponseStatus(HttpStatus.OK)
    public BookingAdvancedDto getById(
            @RequestHeader(name = USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId
    ) {
        log.info("GET /bookings/{}", bookingId);
        return bookingService.getByOwnerId(userId, bookingId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BookingAdvancedDto> getBookingsByStateOfBooker(
            @RequestHeader(name = USER_ID_HEADER) Long bookerId,
            @RequestParam(name = "state", required = false, defaultValue = DEFAULT_STATE) String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        log.info("GET /bookings?state={}&from{}&size{}", state, from, size);
        return bookingService.getAllOfBookerByState(from, size, bookerId, state);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public List<BookingAdvancedDto> getBookingsByStateOfOwner(
            @RequestHeader(name = USER_ID_HEADER) Long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = DEFAULT_STATE) String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size

    ) {
        log.info("GET /bookings/owner?state={}&from{}&size{}", state, from, size);
        return bookingService.getAllOfOwnerByState(from, size, ownerId, state);
    }
}
