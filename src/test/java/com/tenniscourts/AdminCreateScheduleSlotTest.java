package com.tenniscourts;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;

class AdminCreateScheduleSlotTest {

  public static final LocalDateTime NOW = LocalDateTime.now();
  public static final TimeSlot ONE_HOUR_AGO_TIME_SLOT = TimeSlot.of(NOW.minus(1, ChronoUnit.HOURS));
  private final Court arthurAshe = new Court(1L, "Arthur Ashe");

  @Test
  void
      courtSchedulerHasOneCourtScheduleSlot_GivenAnInitialEmptySchedule_WhenAddingOneScheduleForACourt() {
    final var courtScheduler = new CourtScheduler();

    courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(NOW));

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(TimeSlot.of(NOW));
  }

  @Test
  void
      courtSchedulerHasTwoSlotsAvailableForScheduleForSameCourt_GivenInitialEmptySchedule_WhenAddingTwoDifferentTimeSlotsForSameCourt() {
    final var courtScheduler = new CourtScheduler();
    final var currentTimeSlot = TimeSlot.of(NOW);
    courtScheduler.createScheduleSlot(arthurAshe, currentTimeSlot);

    courtScheduler.createScheduleSlot(arthurAshe, ONE_HOUR_AGO_TIME_SLOT);

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(2);

    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(currentTimeSlot);

    final var courtSecondScheduleSlot = courtScheduler.getCourtScheduleSlots().get(1);
    assertThat(courtSecondScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtSecondScheduleSlot.getTimeSlot()).isEqualTo(ONE_HOUR_AGO_TIME_SLOT);
  }
}
