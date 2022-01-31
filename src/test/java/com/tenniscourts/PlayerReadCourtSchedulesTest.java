package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerReadCourtSchedulesTest {

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
        final var currentTimeSlot = TimeSlot.of(LocalTime.now());
        courtScheduler.createScheduleSlot(
                new Court(1L, "Arthur Ashe"), currentTimeSlot, List.of(DayOfWeek.MONDAY));
        final var reservationSystem = new ReservationSystem(courtScheduler);

        assertThat(reservationSystem.getFreeScheduleSlots()).hasSize(1);
        assertThat(reservationSystem.getFreeScheduleSlots().get(0))
                .isEqualTo(new CourtScheduleSlot(new Court(1L, "Arthur Ashe"), currentTimeSlot));
    }
}
