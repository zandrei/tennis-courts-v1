package com.tenniscourts;

import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerReadCourtSchedulesTest {

  @Test
  public void returnsEmptyScheduleSlots_GivenASchedulerThatHasNoEmptySlots() {
    final var courtScheduler = new CourtScheduler();

    assertThat(courtScheduler.getFreeScheduleSlots()).isEmpty();
  }

  private static class CourtScheduler {
    public List<CourtScheduleSlot> getFreeScheduleSlots() {
      return emptyList();
    }
  }

  private static class CourtScheduleSlot {}
}
