package com.tenniscourts;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MakeAPaymentForABookingTest {

    private static final Court ARTHUR_ASHE =
            new Court(1L, "Arthur Ashe", Price.cents(new BigDecimal(100)));
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private static final LocalDate MONDAY = LocalDate.of(2022, 1, 3);
    private static final Player IRRELEVANT_PLAYER = new Player(10L);
    private static final Booking.User IRRELEVANT_USER = new Booking.User(1476L);

    @Test
    @DisplayName("All new bookings are not paid")
    void test() {
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(booking.isPaid()).isFalse();
    }

    @Test
    @DisplayName(
            "Throws PaidAmountDifferentThanRequestedException when trying to make a payment for booking with a different amount")
    @Disabled("Refactoring")
    void test1() {
        final var booking = new Booking(ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);
        final var requestedPaymentAmount = Price.cents(new BigDecimal(983));

        assertThatThrownBy(
                        () ->
                                booking.makePayment(
                                        IRRELEVANT_USER,
                                        requestedPaymentAmount.minusCents(new BigDecimal(200))))
                .isInstanceOf(Booking.PaidAmountDifferentThanRequestedException.class);
    }
}
