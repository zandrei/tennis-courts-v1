package com.tenniscourts;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class AdminCreateScheduleSlotTest {

  @Test
  public void
      courtSchedulerHasOneCourtScheduleSlot_GivenAnInitialEmptySchedule_WhenAddingOneScheduleForACourt() {
    final var courtFinder = mock(CourtFinder.class);
    final var courtScheduler = new CourtScheduler(courtFinder);
    final var arthurAshe = new Court(1L, "Arthur Ashe");

    final var now = LocalDateTime.now();
    courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(now));

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(TimeSlot.of(now));
  }

}
