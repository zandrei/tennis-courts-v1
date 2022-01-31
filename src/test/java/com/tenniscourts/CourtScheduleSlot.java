package com.tenniscourts;

import lombok.EqualsAndHashCode;
import lombok.Value;

@EqualsAndHashCode
@Value
public class CourtScheduleSlot {
    Court court;
    TimeSlot timeSlot;
}
