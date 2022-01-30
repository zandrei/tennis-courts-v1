package com.tenniscourts;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AdminCreateScheduleSlotTest {

  public static final LocalDateTime NOW = LocalDateTime.now();
  public static final TimeSlot ONE_HOUR_AGO_TIME_SLOT = TimeSlot.of(NOW.minus(1, ChronoUnit.HOURS));
  private final Court arthurAshe = new Court(1L, "Arthur Ashe");

  @Test
  @DisplayName(
      "Returns one court schedule slot after adding one schedule for a court, given an initial empty schedule")
  void test() {
    final var courtScheduler = new CourtScheduler();

    courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(NOW));

    assertThat(courtScheduler.getCourtScheduleSlots()).hasSize(1);
    final var courtScheduleSlot = courtScheduler.getCourtScheduleSlots().get(0);
    assertThat(courtScheduleSlot.getCourt()).isEqualTo(arthurAshe);
    assertThat(courtScheduleSlot.getTimeSlot()).isEqualTo(TimeSlot.of(NOW));
  }

  @Test
  @DisplayName(
      "Returns two court schedule slots for the same court, after adding two different time slots for the same court, given an initial empty schedule")
  void test1() {
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

  @Test
  @DisplayName(
      "Throws IllegalArgumentException given an initial empty schedule and trying to add the same time slot for the same court two times")
  void test2() {
    final var courtScheduler = new CourtScheduler();
    courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(NOW));

    assertThatThrownBy(() -> courtScheduler.createScheduleSlot(arthurAshe, TimeSlot.of(NOW)))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
