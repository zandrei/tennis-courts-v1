package com.tenniscourts;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PlayerReadCourtSchedulesTest {

  @Test
  public void returnsEmptyScheduleSlots_GivenASchedulerThatHasNoEmptySlots() {
    final var courtScheduler = new CourtScheduler();

    assertThat(courtScheduler.getFreeScheduleSlots()).isEmpty();
  }
}
