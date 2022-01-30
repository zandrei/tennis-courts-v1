package com.tenniscourts;

import org.junit.Test;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.assertj.core.api.Assertions.assertThat;

public class PlayerReadCourtSchedulesTest {

  @Test
  public void returnsEmptyScheduleSlots_GivenASchedulerThatHasNoEmptySlots() {
    final var reservationSystem = new ReservationSystem();
    assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
  }

  public static class ReservationSystem {

    public List<CourtScheduleSlot> getFreeScheduleSlots() {
      return emptyList();
    }
  }
}
