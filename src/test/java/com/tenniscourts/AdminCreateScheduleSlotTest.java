package com.tenniscourts;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AdminCreateScheduleSlotTest {

    private static final LocalTime NOW = LocalTime.now();
    private static final TimeSlot CURRENT_TIME_SLOT = TimeSlot.of(NOW);
    private static final TimeSlot ONE_HOUR_AGO_TIME_SLOT =
            TimeSlot.of(NOW.minus(1, ChronoUnit.HOURS));
    private final Court arthurAshe = new Court(1L, "Arthur Ashe");
    private final Court rodLaver = new Court(2L, "Rod Laver");

    @Test
    @DisplayName(
            "Returns one court schedule slot after adding one schedule for a court, given an initial empty schedule")
    void test() {
        final var courtScheduler = new CourtScheduler();

        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT);

        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
        final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
        assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
        assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(CURRENT_TIME_SLOT);
    }

    @Test
    @DisplayName(
            "Returns two court schedule slots for the same court, after adding two different time slots for the same court, given an initial empty schedule")
    void test1() {
        final var courtScheduler = new CourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT);

        courtScheduler.createScheduleSlot(arthurAshe, ONE_HOUR_AGO_TIME_SLOT);

        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);

        final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
        assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
        assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(CURRENT_TIME_SLOT);

        final var courtSecondScheduleSlot = courtScheduler.getCourtScheduleSlots().get(1);
        assertThat(courtSecondScheduleSlot.getCourt()).isEqualTo(arthurAshe);
        assertThat(courtSecondScheduleSlot.getTimeSlot()).isEqualTo(ONE_HOUR_AGO_TIME_SLOT);
    }

    @Test
    @DisplayName(
            "Throws IllegalArgumentException given an initial empty schedule and trying to add the same time slot for the same court two times")
    void test2() {
        final var courtScheduler = new CourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT);

        assertThatThrownBy(() -> courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName(
            "Returns two court schedule slots for two different courts, after adding same time slots for two different courts, given an initial empty schedule")
    void test3() {
        final var courtScheduler = new CourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT);
        courtScheduler.createScheduleSlot(rodLaver, CURRENT_TIME_SLOT);

        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);

        assertSchedulerContainsInterestedCourtScheduleSlot(
                courtScheduler, new CourtScheduleSlot(arthurAshe, CURRENT_TIME_SLOT));
        assertSchedulerContainsInterestedCourtScheduleSlot(
                courtScheduler, new CourtScheduleSlot(rodLaver, CURRENT_TIME_SLOT));
    }

    @Test
    @DisplayName("Creates court schedule slots for the same court for MONDAY and WEDNESDAY")
    @Disabled("Refactoring")
    void test4() {
        final var courtScheduler = new CourtScheduler();

        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT, List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));
        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);
    }

    private void assertSchedulerContainsInterestedCourtScheduleSlot(
            CourtScheduler courtScheduler, CourtScheduleSlot interestedCourtScheduleSlot) {
        assertTrue(
                courtScheduler.getCourtScheduleSlots().stream()
                        .anyMatch(
                                courtScheduleSlot ->
                                        courtScheduleSlot.equals(interestedCourtScheduleSlot)));
    }
}
