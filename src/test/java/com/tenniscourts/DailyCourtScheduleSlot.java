package com.tenniscourts;

import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Value
public class DailyCourtScheduleSlot {
    Court court;
    LocalDate day;
    TimeSlot timeSlot;

    boolean isInDayOfWeek(DayOfWeek dayOfWeek) {
        return day.getDayOfWeek().equals(dayOfWeek);
    }
}
