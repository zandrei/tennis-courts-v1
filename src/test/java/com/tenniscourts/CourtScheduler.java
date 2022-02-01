package com.tenniscourts;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class CourtScheduler {

    private final Map<Court, CourtSchedule> schedules = new HashMap<>();

    CourtScheduler() {}

    public List<CourtScheduleSlot> getCourtScheduleSlots() {
        return schedules.values().stream()
                .flatMap(courtSchedule -> courtSchedule.getAllSlots().stream())
                .collect(toList());
    }

    public void createScheduleSlot(
            Court court, TimeSlot timeSlot, List<DayOfWeek> availableForDays) {
        final var courtSchedule = schedules.getOrDefault(court, new CourtSchedule(court));
        courtSchedule.addTimeSlotAt(timeSlot, availableForDays);
        schedules.put(court, courtSchedule);
    }

    public Optional<CourtSchedule> getCourtSchedule(Court court) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getCourt().equals(court))
                .findFirst();
    }

    public List<DailyCourtScheduleSlot> getDailyScheduleSlots(
            LocalDate startInclusive, LocalDate endInclusive) {
        return schedules.values().stream()
                .flatMap(
                        courtSchedule ->
                                courtSchedule
                                        .getDailyScheduleSlots(startInclusive, endInclusive)
                                        .stream())
                .collect(toList());
    }

}
