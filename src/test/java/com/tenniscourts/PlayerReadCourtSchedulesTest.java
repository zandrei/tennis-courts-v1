package com.tenniscourts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PlayerReadCourtSchedulesTest {

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
    assertThat(reservationSystem.getFreeScheduleSlots()).isEmpty();
  }

  @Test
  @DisplayName(
      "Returns one empty schedule slot from the reservation system, given a court scheduler with a single scheduled slot for a court")
  void test1() {
    courtScheduler.createScheduleSlot(
        new Court(1L, "Arthur Ashe"), TimeSlot.of(LocalDateTime.now()));
    final var reservationSystem = new ReservationSystem(courtScheduler);

    assertThat(reservationSystem.getFreeScheduleSlots()).hasSize(1);
    assertThat(reservationSystem.getFreeScheduleSlots().get(0))
        .isEqualTo(
            new CourtScheduleSlot(new Court(1L, "Arthur Ashe"), TimeSlot.of(LocalDateTime.now())));
  }

  public static class ReservationSystem {

    private final CourtScheduler courtScheduler;

    public ReservationSystem(CourtScheduler courtScheduler) {
      this.courtScheduler = courtScheduler;
    }

    public List<CourtScheduleSlot> getFreeScheduleSlots() {
      return courtScheduler.getCourtScheduleSlots();
    }
  }
}
