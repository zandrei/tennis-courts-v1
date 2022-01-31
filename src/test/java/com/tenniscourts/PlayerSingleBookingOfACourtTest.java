package com.tenniscourts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerSingleBookingOfACourtTest {

    private final Court arthurAshe = new Court(1L, "Arthur Ashe");

    @Test
    @DisplayName(
            "Can create a booking for a court at a specific timeslot by a single player,"
                    + "given a reservation system with one available time slot for a court")
    void test() {
        ReservationSystem reservationSystem = new ReservationSystem(new CourtScheduler());

        final var timeslotForNow = TimeSlot.of(LocalDateTime.now());
        reservationSystem.bookCourtForPlayerAtTime(arthurAshe, new Player(10L), timeslotForNow);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(arthurAshe);
        assertThat(allBookingsForCourt).hasSize(1);

        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getCourt()).isEqualTo(arthurAshe);
        assertThat(booking.getPlayer()).isEqualTo(new Player(10L));
        assertThat(booking.getTimeSlot()).isEqualTo(timeslotForNow);
    }
}
