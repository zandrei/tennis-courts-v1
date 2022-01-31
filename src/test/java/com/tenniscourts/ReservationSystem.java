package com.tenniscourts;

import java.util.List;

public class ReservationSystem {

  private final CourtScheduler courtScheduler;

  public ReservationSystem(CourtScheduler courtScheduler) {
    this.courtScheduler = courtScheduler;
  }

  public List<CourtScheduleSlot> getFreeScheduleSlots() {
    return courtScheduler.getCourtScheduleSlots();
  }
}
