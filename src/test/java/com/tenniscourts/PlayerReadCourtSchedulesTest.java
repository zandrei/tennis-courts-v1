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

class PlayerReadCourtSchedulesTest {

    private static final TimeSlot TIME_SLOT_FOR_NOW = TimeSlot.of(LocalTime.now());
    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private CourtScheduler courtScheduler;

    @BeforeEach
    void setUp() {
        courtScheduler = new CourtScheduler();
    }

    @Test
    @DisplayName(
            "Returns empty schedule slots from the reservation system given a court scheduler with no slots created")
    void test() {
        final var reservationSystem = new ReservationSystem(courtScheduler);
        assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
    }

    @Test
    @DisplayName(
            "Returns one free schedule slot from the reservation system, given a court scheduler with a single scheduled slot for a court")
    void test1() {
        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY));
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThat(reservationSystem.getFreeScheduleSlots()).hasSize(1);
        assertThat(reservationSystem.getFreeScheduleSlots().get(0))
                .isEqualTo(new CourtScheduleSlot(ARTHUR_ASHE, TIME_SLOT_FOR_NOW));
    }

    @Test
    @DisplayName(
            "Returns correct free daily schedule slots from the court scheduler for a 1 week requested time interval")
    void test2() {
        final var today = LocalDate.now();
        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY));

        var dailyScheduleSlots =
                courtScheduler.getDailyScheduleSlots(today.minus(7, ChronoUnit.DAYS), today);

        assertThat(dailyScheduleSlots).hasSize(1);
        assertThat(dailyScheduleSlots.get(0).getDay().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.WEDNESDAY));

        dailyScheduleSlots =
                courtScheduler.getDailyScheduleSlots(today.minus(7, ChronoUnit.DAYS), today);

        assertThat(dailyScheduleSlots)
                .hasSize(2)
                .anyMatch(
                        dailySchedule ->
                                dailySchedule.isInDayOfWeek(DayOfWeek.MONDAY))
                .anyMatch(
                        dailySchedule ->
                                dailySchedule.isInDayOfWeek(DayOfWeek.WEDNESDAY));
    }

}
