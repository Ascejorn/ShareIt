package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import ru.practicum.shareit.booking.dto.BookingAdvancedDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.validation.exception.BadRequestException;
import ru.practicum.shareit.validation.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class BookingServiceTest {
    private BookingRepository bookingRepository;
    private ItemService itemService;
    private UserService userService;
    private BookingService bookingService;

    private Long id;
    private Map<Long, Object> longObjectMap;
    private UserDto owner;
    private UserDto booker;
    private ItemDto item;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    void setUp() {
        id = 0L;
        longObjectMap = new HashMap<>();
        userService = Mockito.mock(UserService.class);
        itemService = Mockito.mock(ItemService.class);
        bookingRepository = Mockito.mock(BookingRepository.class);
        Mockito.when(bookingRepository.save(ArgumentMatchers.any()))
                .then(invocation -> {
                    Booking booking = invocation.getArgument(0);
                    if (booking.getId() == null) {
                        booking.setId(++id);
                    }
                    longObjectMap.put(booking.getId(), booking);
                    return booking;
                });
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong()))
                .then(invocation -> {
                    Long id = invocation.getArgument(0);
                    return Optional.ofNullable(longObjectMap.get(id));
                });
        Mockito.when(itemService.create(ArgumentMatchers.any(), ArgumentMatchers.any()))
                .then(invocation -> {
                    ItemDto item = invocation.getArgument(0);
                    if (item.getId() == null) {
                        item.setId(++id);
                    }
                    longObjectMap.put(item.getId(), item);
                    return item;
                });
        Mockito.when(itemService.getByIdOrNotFoundError(ArgumentMatchers.anyLong()))
                .then(invocation -> {
                    Long id = invocation.getArgument(0);
                    Item item = Optional.ofNullable(ItemMapper.toItem((ItemDto) longObjectMap.get(id)))
                            .orElseThrow(() -> new NotFoundException("Not found item #" + id));
                    item.setId(id);
                    item.setOwner(UserMapper.toUser(owner));
                    return item;
                });
        Mockito.when(userService.create(ArgumentMatchers.any()))
                .then(invocation -> {
                    UserDto user = invocation.getArgument(0);
                    if (user.getId() == null) {
                        user.setId(++id);
                    }
                    longObjectMap.put(user.getId(), user);
                    return user;
                });
        Mockito.when(userService.getByIdOrNotFoundError(ArgumentMatchers.anyLong()))
                .then(invocation -> {
                    Long id = invocation.getArgument(0);
                    return Optional.ofNullable(UserMapper.toUser((UserDto) longObjectMap.get(id)))
                            .orElseThrow(() -> new NotFoundException("Not found user #" + id));
                });
        bookingService = new BookingServiceImpl(
                bookingRepository, userService, itemService
        );
        owner = userService.create(UserDto.builder()
                .name("Item Owner")
                .email("item@owner.org")
                .build());
        booker = userService.create(UserDto.builder()
                .name("Item Booker")
                .email("item@booker.org")
                .build());
        item = itemService.create(ItemDto.builder()
                        .name("Wizard's Wand")
                        .description("Wand that allows you to defeat evil")
                        .available(true)
                        .build(),
                owner.getId());
        start = LocalDateTime.now();
        end = start.plusDays(10);
    }

    @Test
    @DisplayName("Get booking by wrong id")
    void whenGetBookingByWrongId_throw404Error() {
        Long wrongId = 999L;
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.getByIdOrNotFoundError(wrongId),
                "Not found booking #" + wrongId
        );
        assertThat(exception.getMessage())
                .isEqualTo("Not found booking #" + wrongId);
    }

    @Test
    @DisplayName("Create new booking")
    void whenCreateNewBookingByDto_returnBookingAdvancedDto() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .build();
        BookingAdvancedDto createdBookingDto = bookingService.create(
                bookingDto, booker.getId()
        );
        assertThat(createdBookingDto).isNotNull();
        assertThat(createdBookingDto.getStatus()).isEqualTo(BookingStatus.WAITING);
        assertThat(createdBookingDto.getStart()).isEqualTo(bookingDto.getStart());
        assertThat(createdBookingDto.getEnd()).isEqualTo(bookingDto.getEnd());
        assertThat(createdBookingDto.getBooker().getId()).isEqualTo(booker.getId());
        assertThat(createdBookingDto.getBooker().getName()).isEqualTo(booker.getName());
        assertThat(createdBookingDto.getBooker().getEmail()).isEqualTo(booker.getEmail());
        assertThat(createdBookingDto.getItem().getId()).isEqualTo(item.getId());
        assertThat(createdBookingDto.getItem().getName()).isEqualTo(item.getName());
        assertThat(createdBookingDto.getItem().getAvailable()).isEqualTo(item.getAvailable());
    }

    @Test
    @DisplayName("Create new booking for a not available item")
    void whenCreateNewBookingForNotAvailableItem_throw400Error() {
        item.setAvailable(false);
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .bookerId(booker.getId())
                .status(BookingStatus.REJECTED)
                .build();
        Mockito.when(userService.getByIdOrNotFoundError(booker.getId()))
                .thenReturn(UserMapper.toUser(booker));
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.create(bookingDto, booker.getId()),
                "Not found unavailable item #" + item.getId()
        );
        assertThat(exception.getMessage())
                .isEqualTo("Not found unavailable item #" + item.getId());
    }

    @Test
    @DisplayName("Create new booking when owner is booker")
    void createBookingWhenOwnerIsBooker_throw404Error() {
        BookingDto bookingDto = BookingDto.builder()
                .itemId(item.getId())
                .start(start)
                .end(end)
                .bookerId(owner.getId())
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(userService.getByIdOrNotFoundError(owner.getId()))
                .thenReturn(UserMapper.toUser(owner));
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.create(bookingDto, owner.getId()),
                "Not found item of user."
        );
        assertThat(exception.getMessage()).isEqualTo("Not found item of user.");
    }

    @Test
    @DisplayName("Approve booking")
    void approveBooking() {
        Item currentItem = ItemMapper.toItem(item);
        currentItem.setOwner(User.builder().id(99L).build());
        Booking booking = Booking.builder()
                .item(currentItem)
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        BookingAdvancedDto bookingDto = bookingService.approve(99L, 1L, true);
        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Get approved but not created booking")
    void getApprovedButNotCreatedBooking() {
        Mockito.when(bookingRepository.findFirstByItem_IdAndBooker_IdAndStatusOrderByStartAsc(
                item.getId(), booker.getId(), BookingStatus.APPROVED
        )).thenReturn(Optional.empty());
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.findApprovedOrNotAvailableError(booker.getId(), item.getId()),
                "Has not approved booking."
        );
        assertThat(exception.getMessage()).isEqualTo("Has not approved booking.");
    }

    @Test
    @DisplayName("Get approved booking")
    void getApprovedBooking() {
        Item currentItem = ItemMapper.toItem(item);
        currentItem.setId(88L);
        currentItem.setOwner(User.builder().id(99L).build());
        Booking booking = Booking.builder()
                .id(99L)
                .item(currentItem)
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.APPROVED)
                .build();
        Mockito.when(bookingRepository.findFirstByItem_IdAndBooker_IdAndStatusOrderByStartAsc(
                currentItem.getId(), booker.getId(), BookingStatus.APPROVED
        )).thenReturn(Optional.ofNullable(booking));
        Booking returnedBooking = bookingService.findApprovedOrNotAvailableError(booker.getId(), currentItem.getId());
        assertThat(returnedBooking).isNotNull();
        assertThat(returnedBooking.getId()).isEqualTo(booking.getId());
        assertThat(returnedBooking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    @DisplayName("Rejected booking")
    void rejectedBooking() {
        Item currentItem = ItemMapper.toItem(item);
        currentItem.setOwner(User.builder().id(99L).build());
        Booking booking = Booking.builder()
                .id(1L)
                .item(currentItem)
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        BookingAdvancedDto bookingDto = bookingService.approve(99L, 1L, false);
        assertThat(bookingDto).isNotNull();
        assertThat(bookingDto.getStatus()).isEqualTo(BookingStatus.REJECTED);
    }

    @Test
    @DisplayName("Approve booking when booking is already approved")
    void approveBookingIfAlreadyApproved_throw400Error() {
        Booking booking = Booking.builder()
                .item(ItemMapper.toItem(item))
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.APPROVED)
                .build();
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        BadRequestException exception = assertThrows(
                BadRequestException.class,
                () -> bookingService.approve(owner.getId(), 1L, true),
                "Booking already approved/rejected/canceled."
        );
        assertThat(exception.getMessage()).isEqualTo("Booking already approved/rejected/canceled.");
    }

    @Test
    @DisplayName("Approve booking when item of other owner")
    void approveBookingWhenItemOfOtherOwner_throw404Error() {
        Booking booking = Booking.builder()
                .id(10L)
                .item(Item.builder().owner(UserMapper.toUser(booker)).build())
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.WAITING)
                .build();
        Mockito.when(bookingRepository.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.ofNullable(booking));
        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> bookingService.approve(owner.getId(), 10L, true),
                "Not found booking #" + booking.getId()
        );
        assertThat(exception.getMessage()).isEqualTo("Not found booking #" + booking.getId());
    }

    @Test
    @DisplayName("Get by owner id")
    void getByOwnerId_returnBookingDto() {
        Item currentItem = Item.builder()
                .id(item.getId())
                .owner(UserMapper.toUser(owner))
                .build();
        Booking booking = Booking.builder()
                .item(currentItem)
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.WAITING)
                .build();
        BookingAdvancedDto bookingDto = bookingService.create(
                BookingMapper.toBookingDto(booking), booker.getId()
        );
        BookingAdvancedDto bookingByOwner = bookingService.getByOwnerId(
                owner.getId(), bookingDto.getId()
        );
        assertThat(bookingByOwner).isNotNull();
        assertThat(bookingByOwner.getId()).isEqualTo(bookingDto.getId());
    }

    @Test
    void getByOwnerId() {
        Item currentItem = Item.builder()
                .id(item.getId())
                .owner(UserMapper.toUser(owner))
                .build();
        Booking booking = Booking.builder()
                .item(currentItem)
                .start(start)
                .end(end)
                .booker(UserMapper.toUser(booker))
                .status(BookingStatus.WAITING)
                .build();
        BookingAdvancedDto bookingDto = bookingService.create(
                BookingMapper.toBookingDto(booking), booker.getId()
        );
        BookingAdvancedDto bookingByOwner = bookingService.getByOwnerId(
                owner.getId(), bookingDto.getId()
        );
        assertThat(bookingByOwner).isNotNull();
        assertThat(bookingByOwner.getId()).isEqualTo(bookingDto.getId());
    }
}