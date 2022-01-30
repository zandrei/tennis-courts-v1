package com.tenniscourts;

public class CourtScheduleSlot {
  private Court court;
  private TimeSlot timeSlot;

  public CourtScheduleSlot(Court court, TimeSlot timeSlot) {
    this.court = court;
    this.timeSlot = timeSlot;
  }

  public Court getCourt() {
    return court;
  }

  public TimeSlot getTimeSlot() {
    return timeSlot;
  }
}
