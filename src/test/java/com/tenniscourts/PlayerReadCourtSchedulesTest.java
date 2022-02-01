package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class PlayerReadCourtSchedulesTest {

    private static final TimeSlot TIME_SLOT_FOR_NOW = TimeSlot.of(LocalTime.now());
    private static final Court ARTHUR_ASHE = new Court(1L, "Arthur Ashe");
    private static final Court ROD_LAVER = new Court(2L, "Rod Laver");
    private static final LocalDate TODAY = LocalDate.now();
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
        assertThat(reservationSystem.getFreeDailyScheduleSlots(TODAY, TODAY)).isEmpty();
    }

    @Test
    @DisplayName(
            "Returns one free schedule slot from the reservation system, given a court scheduler with a single scheduled slot for a court")
    void test1() {
        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY));
        final var reservationSystem = new ReservationSystem(courtScheduler);

        final var freeDailyScheduleSlots =
                reservationSystem.getFreeDailyScheduleSlots(TODAY, TODAY.plusDays(7));
        assertThat(freeDailyScheduleSlots).hasSize(1);
        final var freeSlot = freeDailyScheduleSlots.get(0);
        assertThat(freeSlot.getTimeSlot()).isEqualTo(TIME_SLOT_FOR_NOW);
        assertThat(freeSlot.getCourt()).isEqualTo(ARTHUR_ASHE);
    }

    @Test
    @DisplayName(
            "Returns correct free daily schedule slots from the court scheduler for a 1 week requested time interval")
    void test2() {
        final var today = TODAY;
        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY));

        var dailyScheduleSlots = courtScheduler.getDailyScheduleSlots(today.minusDays(7), today);

        assertThat(dailyScheduleSlots).hasSize(1);
        assertThat(dailyScheduleSlots.get(0).getDay().getDayOfWeek()).isEqualTo(DayOfWeek.MONDAY);

        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.WEDNESDAY));

        dailyScheduleSlots = courtScheduler.getDailyScheduleSlots(today.minusDays(7), today);

        assertThat(dailyScheduleSlots)
                .hasSize(2)
                .anyMatch(dailySchedule -> dailySchedule.isInDayOfWeek(DayOfWeek.MONDAY))
                .anyMatch(dailySchedule -> dailySchedule.isInDayOfWeek(DayOfWeek.WEDNESDAY));
    }

    @Test
    @DisplayName(
            "Returns correct free daily schedule slots from the court scheduler for a 1 week requested time interval given schedules for two courts")
    void test3() {
        final var today = TODAY;
        courtScheduler.createScheduleSlot(
                ARTHUR_ASHE, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY));

        courtScheduler.createScheduleSlot(
                ROD_LAVER, TIME_SLOT_FOR_NOW, List.of(DayOfWeek.MONDAY, DayOfWeek.FRIDAY));

        var dailyScheduleSlots = courtScheduler.getDailyScheduleSlots(today, today.plusDays(14));

        assertThat(dailyScheduleSlots).hasSize(6);

        final var arthurAsheSlots =
                dailyScheduleSlots.stream()
                        .filter(
                                dailyCourtScheduleSlot ->
                                        dailyCourtScheduleSlot.getCourt().equals(ARTHUR_ASHE))
                        .collect(toList());
        final var rodLaverSlots =
                dailyScheduleSlots.stream()
                        .filter(
                                dailyCourtScheduleSlot ->
                                        dailyCourtScheduleSlot.getCourt().equals(ROD_LAVER))
                        .collect(toList());

        verifyCourtScheduleSlotsAreForExpectedProperties(
                TIME_SLOT_FOR_NOW, ARTHUR_ASHE, arthurAsheSlots);

        assertThat(rodLaverSlots).hasSize(4);
        final var mondayRodLaverSlots =
                rodLaverSlots.stream()
                        .filter(slot -> slot.isInDayOfWeek(DayOfWeek.MONDAY))
                        .collect(toList());
        final var fridayRodLaverSlots =
                rodLaverSlots.stream()
                        .filter(slot -> slot.isInDayOfWeek(DayOfWeek.FRIDAY))
                        .collect(toList());

        assertThat(mondayRodLaverSlots).hasSize(2);
        assertThat(fridayRodLaverSlots).hasSize(2);

        verifyCourtScheduleSlotsAreForExpectedProperties(
                TIME_SLOT_FOR_NOW, ROD_LAVER, mondayRodLaverSlots);
        verifyCourtScheduleSlotsAreForExpectedProperties(
                TIME_SLOT_FOR_NOW, ROD_LAVER, fridayRodLaverSlots);
    }

    private void verifyCourtScheduleSlotsAreForExpectedProperties(
            TimeSlot expectedTimeSlot,
            Court expectedCourt,
            List<DailyCourtScheduleSlot> courtScheduleSlots) {
        assertThat(courtScheduleSlots).hasSize(2);
        final var firstArthurAsheSlot = courtScheduleSlots.get(0);
        final var secondArthurAsheSlot = courtScheduleSlots.get(1);

        assertThat(firstArthurAsheSlot.getDay()).isNotEqualTo(secondArthurAsheSlot.getDay());
        assertThat(firstArthurAsheSlot.getTimeSlot())
                .isEqualTo(secondArthurAsheSlot.getTimeSlot())
                .isEqualTo(expectedTimeSlot);
        assertThat(firstArthurAsheSlot.getCourt())
                .isEqualTo(secondArthurAsheSlot.getCourt())
                .isEqualTo(expectedCourt);
    }
}
