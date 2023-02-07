package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;
    public static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<Object> createBooking(
            @Valid @RequestBody BookingDto bookingDto,
            @RequestHeader(name = USER_ID_HEADER) Long bookerId
    ) {
        log.info("POST /bookings {}", bookingDto);
        return bookingClient.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(
            @RequestHeader(name = USER_ID_HEADER) Long ownerId,
            @RequestParam boolean approved,
            @PathVariable Long bookingId
    ) {
        log.info("PATCH /bookings/{}?approved={}", bookingId, approved);
        return bookingClient.approveBooking(ownerId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingByOwnerId(
            @RequestHeader(name = USER_ID_HEADER) Long userId,
            @PathVariable Long bookingId
    ) {
        log.info("GET /bookings/{}", bookingId);
        return bookingClient.getBookingByOwnerId(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByStateOfBooker(
            @RequestHeader(name = USER_ID_HEADER) Long bookerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        log.info("GET /bookings?state={}&from{}&size{}", state, from, size);
        return bookingClient.getBookingsByStateOfBooker(from, size, bookerId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingsByStateOfOwner(
            @RequestHeader(name = USER_ID_HEADER) Long ownerId,
            @RequestParam(name = "state", required = false, defaultValue = "ALL") String state,
            @RequestParam(name = "from", defaultValue = "0") int from,
            @RequestParam(name = "size", defaultValue = "20") int size

    ) {
        log.info("GET /bookings/owner?state={}&from{}&size{}", state, from, size);
        return bookingClient.getBookingsByStateOfOwner(from, size, ownerId, state);
    }
}
