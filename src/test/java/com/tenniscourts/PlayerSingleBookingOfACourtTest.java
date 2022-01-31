package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerSingleBookingOfACourtTest {

    private final Court arthurAshe = new Court(1L, "Arthur Ashe");
    private CourtScheduler courtScheduler;

    @BeforeEach
    void setUp() {
        courtScheduler = new CourtScheduler();
    }

    @Test
    @DisplayName(
            "Can create a booking for a court at a specific timeslot by a single player,"
                    + "given a reservation system with one available time slot for a court")
    void test() {
        final var timeslotForNow = TimeSlot.of(LocalTime.now());
        courtScheduler.createScheduleSlot(arthurAshe, timeslotForNow, List.of(DayOfWeek.MONDAY));
        ReservationSystem reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                arthurAshe, new Player(10L), LocalDate.now(), timeslotForNow);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(arthurAshe);
        assertThat(allBookingsForCourt).hasSize(1);

        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getCourt()).isEqualTo(arthurAshe);
        assertThat(booking.getPlayer()).isEqualTo(new Player(10L));
        assertThat(booking.getTimeSlot()).isEqualTo(timeslotForNow);
    }
}
