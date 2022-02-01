package com.tenniscourts;

import lombok.Value;

import java.time.LocalDate;

@Value
public class DailyCourtScheduleSlot {
    Court court;
    LocalDate day;
    TimeSlot timeSlot;
}
