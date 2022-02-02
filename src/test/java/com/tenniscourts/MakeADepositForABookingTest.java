package com.tenniscourts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MakeADepositForABookingTest {

    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private static final LocalDate MONDAY = LocalDate.of(2022, 1, 3);
    private static final Player IRRELEVANT_PLAYER = new Player(10L);
    private static final Booking.User IRRELEVANT_USER = new Booking.User(255L);

    @Test
    @DisplayName("Is deposit paid returns true for a booking for which a deposit was made")
    void test() {
        final var irrelevantPrice = Price.cents(new BigDecimal(983));
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);
        booking.createDepositRequest(irrelevantPrice);

        booking.makeDeposit(IRRELEVANT_USER, irrelevantPrice);

        assertThat(booking.isDepositPaid()).isTrue();
    }

    @Test
    @DisplayName("Is deposit paid returns false for a booking for which no deposit was made")
    void test1() {
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(booking.isDepositPaid()).isFalse();
    }

    @Test
    @DisplayName(
            "Throws IllegalStateException if make deposit is called and no deposit request is present")
    void test2() {
        final var irrelevantPrice = Price.cents(new BigDecimal(1099));
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThatThrownBy(() -> booking.makeDeposit(IRRELEVANT_USER, irrelevantPrice))
                .isInstanceOf(IllegalStateException.class);
    }
}
