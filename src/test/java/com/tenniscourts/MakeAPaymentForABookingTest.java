package com.tenniscourts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class MakeAPaymentForABookingTest {

    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private static final LocalDate MONDAY = LocalDate.of(2022, 1, 3);
    private static final Player IRRELEVANT_PLAYER = new Player(10L);

    @Test
    @DisplayName("All new bookings are not paid")
    void test() {
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(booking.isPaid()).isFalse();
    }
}
