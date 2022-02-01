package com.tenniscourts;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CourtScheduler {
    List<CourtScheduleSlot> getCourtScheduleSlots();

    void createScheduleSlot(
            Court court, TimeSlot timeSlot, List<DayOfWeek> availableForDays);

    Optional<CourtSchedule> getCourtSchedule(Court court);

    List<DailyCourtScheduleSlot> getDailyScheduleSlots(
            LocalDate startInclusive, LocalDate endInclusive);
}
