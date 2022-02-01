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

class PlayerSingleBookingOfACourtTest {

    private static final Player IRRELEVANT_PLAYER = new Player(10L);
    private static final LocalDate NOT_MONDAY = LocalDate.of(2022, 1, 1);
    private static final LocalDate MONDAY = LocalDate.of(2022, 1, 3);
    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private static final Court NOT_ARTHUR_ASHE = new Court(2L, "Rod Laver");
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private static final TimeSlot NON_EXISTANT_TIMESLOT =
            TimeSlot.of(LocalTime.now().minus(1, ChronoUnit.HOURS));
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
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForCourt).hasSize(1);

        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getCourt()).isEqualTo(ARTHUR_ASHE);
        assertThat(booking.getPlayer()).isEqualTo(IRRELEVANT_PLAYER);
        assertThat(booking.getTimeSlot()).isEqualTo(EXISTING_TIMESLOT);
    }

    @Test
    @DisplayName(
            "Throws IllegalArgumentException when trying to create a booking for a specific timeslot "
                    + "given a reservation system with no available timeslot at the requested time")
    void test1() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        ARTHUR_ASHE,
                                        IRRELEVANT_PLAYER,
                                        NOT_MONDAY,
                                        EXISTING_TIMESLOT))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        ARTHUR_ASHE,
                                        IRRELEVANT_PLAYER,
                                        MONDAY,
                                        NON_EXISTANT_TIMESLOT))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        NOT_ARTHUR_ASHE,
                                        IRRELEVANT_PLAYER,
                                        MONDAY,
                                        EXISTING_TIMESLOT))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
