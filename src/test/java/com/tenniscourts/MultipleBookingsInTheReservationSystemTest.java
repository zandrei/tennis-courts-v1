package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultipleBookingsInTheReservationSystemTest {

    private static final Player IRRELEVANT_PLAYER = new Player(10L);
    private static final LocalDate MONDAY = LocalDate.of(2022, 1, 3);
    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private static final Court NOT_ARTHUR_ASHE = new Court(2L, "Not Arthur Ashe");
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private final List<DayOfWeek> ONLY_MONDAY = List.of(DayOfWeek.MONDAY);
    private final List<DayOfWeek> MONDAY_AND_WEDNESDAY =
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    private CourtScheduler courtScheduler;

    @BeforeEach
    void setUp() {
        courtScheduler = new CourtScheduler();
    }

    @Test
    @DisplayName("Creates a booking for a court given a court scheduler with one schedule slot")
    void test() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForCourt).hasSize(1);
    }

    @Test
    @DisplayName(
            "Throws an IllegalArgumentException trying to book a court which does not have a schedule slot")
    void test1() {

        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        NOT_ARTHUR_ASHE,
                                        IRRELEVANT_PLAYER,
                                        MONDAY,
                                        EXISTING_TIMESLOT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Creates two bookings for a court given a court scheduler with two schedule slot")
    void test2() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, MONDAY_AND_WEDNESDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThat(reservationSystem.getFreeScheduleSlots()).hasSize(2);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY.plus(2, ChronoUnit.DAYS), EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForCourt).hasSize(2);
    }
}
