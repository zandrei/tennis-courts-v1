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
    private final Court arthurAshe = new Court(1L, "Arthur Ashe");
    private static final TimeSlot TIMESLOT_FOR_NOW = TimeSlot.of(LocalTime.now());
    private static final TimeSlot TIMESLOT_FOR_ONE_HOUR_AGO =
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
        courtScheduler.createScheduleSlot(arthurAshe, TIMESLOT_FOR_NOW, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                arthurAshe, IRRELEVANT_PLAYER, LocalDate.now(), TIMESLOT_FOR_NOW);

        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(arthurAshe);
        assertThat(allBookingsForCourt).hasSize(1);

        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getCourt()).isEqualTo(arthurAshe);
        assertThat(booking.getPlayer()).isEqualTo(IRRELEVANT_PLAYER);
        assertThat(booking.getTimeSlot()).isEqualTo(TIMESLOT_FOR_NOW);
    }

    @Test
    @DisplayName(
            "Throws IllegalArgumentException when trying to create a booking for a specific timeslot "
                    + "given a reservation system with no available timeslot at the requested time")
    void test1() {
        courtScheduler.createScheduleSlot(arthurAshe, TIMESLOT_FOR_NOW, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        arthurAshe,
                                        IRRELEVANT_PLAYER,
                                        NOT_MONDAY,
                                        TIMESLOT_FOR_NOW))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(
                        () ->
                                reservationSystem.bookCourtForPlayerOnDateAtTime(
                                        arthurAshe,
                                        IRRELEVANT_PLAYER,
                                        MONDAY,
                                        TIMESLOT_FOR_ONE_HOUR_AGO))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
