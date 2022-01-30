package com.tenniscourts;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class AdminCreateScheduleSlotTest {

  @Test
  public void
      courtSchedulerHasOneCourtScheduleSlot_GivenAnInitialEmptySchedule_WhenAddingOneScheduleForACourt() {
    final var courtScheduler = new CourtScheduler();
    final var arthurAshe = new Court(1L, "Arthur Ashe");

    final var now = LocalDateTime.now();
    courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(now));

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(TimeSlot.of(now));
  }

  @Test
  public void courtSchedulerHasTwoSlotsAvailableForScheduleForSameCourt_GivenInitialEmptySchedule_WhenAddingTwoDifferentTimeSlotsForSameCourt() {
    final var courtScheduler = new CourtScheduler();
    final var arthurAshe = new Court(1L, "Arthur Ashe");

    final var now = LocalDateTime.now();

    final var currentTimeSlot = TimeSlot.of(now);
    courtScheduler.createScheduleSlot(arthurAshe, currentTimeSlot);

    final var oneHourAgoTimeSlot = TimeSlot.of(now.minus(1, ChronoUnit.HOURS));
    courtScheduler.createScheduleSlot(arthurAshe, oneHourAgoTimeSlot);

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);

    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(currentTimeSlot);

    final var courtSecondScheduleSlot = courtScheduler.getCourtScheduleSlots().get(1);
    assertThat(courtSecondScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtSecondScheduleSlot.getTimeSlot()).isEqualTo(oneHourAgoTimeSlot);
  }
}
