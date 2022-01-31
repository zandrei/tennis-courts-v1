package com.tenniscourts;

import java.time.LocalDateTime;
import java.util.Objects;

public class TimeSlot {
    private final LocalDateTime startTime;

    TimeSlot(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public static TimeSlot of(LocalDateTime startTime) {
        return new TimeSlot(startTime);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeSlot timeSlot = (TimeSlot) o;
        return startTime.equals(timeSlot.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startTime);
    }
}
