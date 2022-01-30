package com.tenniscourts;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class PlayerReadCourtSchedulesTest {

  @Test
  public void returnsEmptyScheduleSlots_GivenASchedulerThatHasNoEmptySlots() {
    final var courtScheduler =
        new CourtScheduler(mock(CourtFinder.class));

    assertThat(courtScheduler.getFreeScheduleSlots()).isEmpty();
  }
}
