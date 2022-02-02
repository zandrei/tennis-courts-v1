package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    private static final LocalDate WEDNESDAY = MONDAY.plusDays(2);
    private static final Court ARTHUR_ASHE =
            new Court(1L, "Arthur Ashe", Price.cents(new BigDecimal(332)));
    private static final Court NOT_ARTHUR_ASHE =
            new Court(2L, "Not Arthur Ashe", Price.cents(new BigDecimal(4560)));
    private static final TimeSlot EXISTING_TIMESLOT = TimeSlot.of(LocalTime.now());
    private final List<DayOfWeek> ONLY_MONDAY = List.of(DayOfWeek.MONDAY);
    private final List<DayOfWeek> MONDAY_AND_WEDNESDAY =
            List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY);
    private CourtScheduler courtScheduler;

    @BeforeEach
    void setUp() {
        courtScheduler = new InMemoryCourtScheduler();
    }

    @Test
    @DisplayName("Creates a booking for a court given a court scheduler with one schedule slot")
    void test() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, MONDAY)).isEmpty();
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

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, WEDNESDAY)).hasSize(2);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY.plus(2, ChronoUnit.DAYS), EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, WEDNESDAY)).isEmpty();
        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForCourt).hasSize(2);
    }

    @Test
    @DisplayName(
            "Creates two bookings for two courts given a court scheduler with two schedule slots for two different courts")
    void test3() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        courtScheduler.createScheduleSlot(NOT_ARTHUR_ASHE, EXISTING_TIMESLOT, ONLY_MONDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, MONDAY)).hasSize(2);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                NOT_ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, MONDAY)).isEmpty();
        final var allBookingsForArthurAshe = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForArthurAshe).hasSize(1);

        final var allBookingsForOtherCourt =
                reservationSystem.getAllBookingsForCourt(NOT_ARTHUR_ASHE);
        assertThat(allBookingsForOtherCourt).hasSize(1);
    }

    @Test
    @DisplayName(
            "Shows correct free booking slots given a court scheduler with two available slots after a single court slot is booked")
    void test4() {
        courtScheduler.createScheduleSlot(ARTHUR_ASHE, EXISTING_TIMESLOT, MONDAY_AND_WEDNESDAY);
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThat(reservationSystem.getFreeDailyScheduleSlots(MONDAY, WEDNESDAY)).hasSize(2);
        reservationSystem.bookCourtForPlayerOnDateAtTime(
                ARTHUR_ASHE, IRRELEVANT_PLAYER, MONDAY, EXISTING_TIMESLOT);

        final var freeScheduleSlots =
                reservationSystem.getFreeDailyScheduleSlots(MONDAY, WEDNESDAY);
        assertThat(freeScheduleSlots).hasSize(1);
        assertThat(freeScheduleSlots.get(0).getCourt()).isEqualTo(ARTHUR_ASHE);
        assertThat(freeScheduleSlots.get(0).getTimeSlot()).isEqualTo(EXISTING_TIMESLOT);
        assertThat(freeScheduleSlots.get(0).getDay()).isEqualTo(WEDNESDAY);

        final var allBookingsForCourt = reservationSystem.getAllBookingsForCourt(ARTHUR_ASHE);
        assertThat(allBookingsForCourt).hasSize(1);
        final var booking = allBookingsForCourt.get(0);
        assertThat(booking.getPlayer()).isEqualTo(IRRELEVANT_PLAYER);
        assertThat(booking.getBookingDate()).isEqualTo(MONDAY);
        assertThat(booking.getTimeSlot()).isEqualTo(EXISTING_TIMESLOT);
        assertThat(booking.getCourt()).isEqualTo(ARTHUR_ASHE);
    }
}
