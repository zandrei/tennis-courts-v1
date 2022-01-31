package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PlayerSingleBookingOfACourtTest {

    public static final Player IRRELEVANT_PLAYER = new Player(10L);
    public static final LocalDate NOT_MONDAY = LocalDate.of(2022, 1, 1);
    private final Court arthurAshe = new Court(1L, "Arthur Ashe");
    private final TimeSlot timeslotForNow = TimeSlot.of(LocalTime.now());
    private final List<DayOfWeek> ONLY_MONDAY = List.of(DayOfWeek.MONDAY);
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
        courtScheduler.createScheduleSlot(arthurAshe, timeslotForNow, ONLY_MONDAY);
        ReservationSystem reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                arthurAshe, IRRELEVANT_PLAYER, LocalDate.now(), timeslotForNow);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(arthurAshe);
        assertThat(allBookingsForCourt).hasSize(1);

        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getCourt()).isEqualTo(arthurAshe);
        assertThat(booking.getPlayer()).isEqualTo(IRRELEVANT_PLAYER);
        assertThat(booking.getTimeSlot()).isEqualTo(timeslotForNow);
    }

    @Test
    @DisplayName(
            "Throws IllegalArgumentException when trying to create a booking for a specific timeslot given a reservation system with no available timeslot at the requested time")
    void test1() {
        courtScheduler.createScheduleSlot(arthurAshe, timeslotForNow, ONLY_MONDAY);
        ReservationSystem reservationSystem = new ReservationSystem(courtScheduler);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        arthurAshe, IRRELEVANT_PLAYER, NOT_MONDAY, timeslotForNow))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
