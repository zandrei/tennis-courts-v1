package com.tenniscourts;

import lombok.Getter;

import java.time.DayOfWeek;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CourtScheduler {

    private final HashMap<Court, List<CourtScheduleSlot>> courtScheduleSlots = new HashMap<>();
    private final Set<CourtSchedule> schedules = new HashSet<>();

    CourtScheduler() {}

    public void createScheduleSlot(Court court, TimeSlot timeSlot) {
        if (timeSlotIsInvalidForCourt(court, timeSlot)) {
            throw new IllegalArgumentException(
                    "Cannot add the same time slot multiple times for a single court!");
        }
        final var courtScheduleSlot = new CourtScheduleSlot(court, timeSlot);
        courtScheduleSlots.putIfAbsent(court, new ArrayList<>());
        courtScheduleSlots.get(court).add(courtScheduleSlot);
    }

    private boolean timeSlotIsInvalidForCourt(Court court, TimeSlot timeSlot) {
        return courtScheduleSlots.containsKey(court)
                && courtScheduleSlots.get(court).contains(new CourtScheduleSlot(court, timeSlot));
    }

    public List<CourtScheduleSlot> getCourtScheduleSlots() {
        return courtScheduleSlots.values().stream().flatMap(Collection::stream).collect(toList());
    }

    public void createScheduleSlot(
            Court court, TimeSlot timeSlot, List<DayOfWeek> availableForDays) {
        final var courtSchedule = new CourtSchedule(court);
        courtSchedule.addTimeSlotAt(timeSlot, availableForDays);
        schedules.add(courtSchedule);
    }

    public Optional<CourtSchedule> getCourtSchedule(Court court) {
        return schedules.stream().filter(schedule -> schedule.getCourt().equals(court)).findFirst();
    }

    public static class CourtSchedule {
        @Getter private final Court court;
        private final Map<DayOfWeek, List<CourtScheduleSlot>> dailySlots = new HashMap<>();

        public CourtSchedule(Court court) {
            this.court = court;
        }

        public void addTimeSlotAt(TimeSlot timeSlot, List<DayOfWeek> availableOnDays) {
            availableOnDays.forEach(
                    dayAvailable -> {
                        dailySlots.putIfAbsent(dayAvailable, new ArrayList<>());
                        dailySlots.get(dayAvailable).add(new CourtScheduleSlot(court, timeSlot));
                    });
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CourtSchedule that = (CourtSchedule) o;
            return court.equals(that.court);
        }

        @Override
        public int hashCode() {
            return Objects.hash(court);
        }

        public List<CourtScheduleSlot> getScheduleSlotsForDay(DayOfWeek dayOfWeek) {
            return dailySlots.getOrDefault(dayOfWeek, emptyList());
        }
    }
}
