package com.tenniscourts;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.LocalDate;

@EqualsAndHashCode
@Value
public class CourtScheduleSlot {
    Court court;
    TimeSlot timeSlot;

    public DailyCourtScheduleSlot atDay(LocalDate day) {
        return new DailyCourtScheduleSlot(court, day, timeSlot);
    }
}
