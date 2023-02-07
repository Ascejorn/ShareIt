package ru.practicum.shareit.booking.dto;

public enum BookingState {
    ALL,
    CURRENT,
    FUTURE,
    PAST,
    REJECTED,
    WAITING,
    UNSUPPORTED;

    public static BookingState from(String stateText) {
        for (BookingState state : BookingState.values()) {
            if (state.name().equalsIgnoreCase(stateText)) {
                return state;
            }
        }
        return BookingState.UNSUPPORTED;
    }
}
