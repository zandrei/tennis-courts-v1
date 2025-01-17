package com.tenniscourts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
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
    public static final List<DayOfWeek> ONLY_MONDAY = List.of(DayOfWeek.MONDAY);
    private final Court arthurAshe =
            new Court(1L, "Arthur Ashe", Price.cents(new BigDecimal(3243)));
    private final Court rodLaver = new Court(2L, "Rod Laver", Price.cents(new BigDecimal(2223)));

    @Test
    @DisplayName(
            "Returns one court schedule slot after adding one schedule for a court, given an initial empty schedule")
    void test() {
        final var courtScheduler = new InMemoryCourtScheduler();

        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT, ONLY_MONDAY);

        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
        final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
        assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
        assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(CURRENT_TIME_SLOT);
    }

    @Test
    @DisplayName(
            "Returns two court schedule slots for the same court, after adding two different time slots for the same court, given an initial empty schedule")
    void test1() {
        final var courtScheduler = new InMemoryCourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT, ONLY_MONDAY);

        courtScheduler.createScheduleSlot(arthurAshe, ONE_HOUR_AGO_TIME_SLOT, ONLY_MONDAY);

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
        final var courtScheduler = new InMemoryCourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT, ONLY_MONDAY);

        assertThatThrownBy(
                        () ->
                                courtScheduler.createScheduleSlot(
                                        arthurAshe, CURRENT_TIME_SLOT, ONLY_MONDAY))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName(
            "Returns two court schedule slots for two different courts, after adding same time slots for two different courts, given an initial empty schedule")
    void test3() {
        final var courtScheduler = new InMemoryCourtScheduler();
        courtScheduler.createScheduleSlot(arthurAshe, CURRENT_TIME_SLOT, ONLY_MONDAY);
        courtScheduler.createScheduleSlot(rodLaver, CURRENT_TIME_SLOT, ONLY_MONDAY);

        assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);

        assertSchedulerContainsInterestedCourtScheduleSlot(
                courtScheduler, new CourtScheduleSlot(arthurAshe, CURRENT_TIME_SLOT));
        assertSchedulerContainsInterestedCourtScheduleSlot(
                courtScheduler, new CourtScheduleSlot(rodLaver, CURRENT_TIME_SLOT));
    }

    @Test
    @DisplayName(
            "Adds a court schedule slot at a requested time slot for each of the days of week given as parameters")
    void test4() {
        final var courtScheduler = new InMemoryCourtScheduler();

        courtScheduler.createScheduleSlot(
                arthurAshe, CURRENT_TIME_SLOT, List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY));

        final var courtScheduleOpt = courtScheduler.getCourtSchedule(arthurAshe);
        assertThat(courtScheduleOpt).isPresent();
        final var courtSchedule = courtScheduleOpt.get();
        assertThat(courtSchedule.getCourt()).isEqualTo(arthurAshe);

        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.MONDAY)).hasSize(1);
        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.WEDNESDAY)).hasSize(1);

        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.THURSDAY)).isEmpty();
        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.TUESDAY)).isEmpty();
        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.FRIDAY)).isEmpty();
        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.SATURDAY)).isEmpty();
        assertThat(courtSchedule.getScheduleSlotsForDay(DayOfWeek.SUNDAY)).isEmpty();
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
