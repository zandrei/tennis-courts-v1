package com.tenniscourts;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class InMemoryCourtScheduler implements CourtScheduler {

    private final Map<Court, CourtSchedule> schedules = new HashMap<>();

    InMemoryCourtScheduler() {}

    @Override
    public List<CourtScheduleSlot> getCourtScheduleSlots() {
        return schedules.values().stream()
                .flatMap(courtSchedule -> courtSchedule.getAllSlots().stream())
                .collect(toList());
    }

    @Override
    public void createScheduleSlot(
            Court court, TimeSlot timeSlot, List<DayOfWeek> availableForDays) {
        final var courtSchedule = schedules.getOrDefault(court, new CourtSchedule(court));
        courtSchedule.addTimeSlotAt(timeSlot, availableForDays);
        schedules.put(court, courtSchedule);
    }

    @Override
    public Optional<CourtSchedule> getCourtSchedule(Court court) {
        return schedules.values().stream()
                .filter(schedule -> schedule.getCourt().equals(court))
                .findFirst();
    }

    @Override
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
