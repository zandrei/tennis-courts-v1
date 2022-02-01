package com.tenniscourts;

import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CourtSchedule {
    @Getter private final Court court;
    private final Map<DayOfWeek, List<CourtScheduleSlot>> dailySlots = new HashMap<>();

    public CourtSchedule(Court court) {
        this.court = court;
    }

    public List<DailyCourtScheduleSlot> getDailyScheduleSlots(
            LocalDate startInclusive, LocalDate endInclusive) {

        return generateDaysBetween(startInclusive, endInclusive).stream()
                .filter(day -> dailySlots.containsKey(day.getDayOfWeek()))
                .flatMap(
                        day ->
                                dailySlots.get(day.getDayOfWeek()).stream()
                                        .map(courtScheduleSlot -> courtScheduleSlot.atDay(day)))
                .collect(toList());
    }

    private List<LocalDate> generateDaysBetween(LocalDate start, LocalDate end) {
        List<LocalDate> daysBetween = new ArrayList<>();
        while (!start.isAfter(end)) {
            daysBetween.add(start);
            start = start.plusDays(1);
        }
        return daysBetween;
    }

    public void addTimeSlotAt(TimeSlot timeSlot, List<DayOfWeek> availableOnDays) {
        if (timeSlotIsInvalidForGivenDays(timeSlot, availableOnDays)) {
            throw new IllegalArgumentException(
                    "Cannot add the same time slot multiple times for a single court!");
        }
        availableOnDays.forEach(
                dayAvailable -> {
                    dailySlots.putIfAbsent(dayAvailable, new ArrayList<>());
                    dailySlots.get(dayAvailable).add(new CourtScheduleSlot(court, timeSlot));
                });
    }

    private boolean timeSlotIsInvalidForGivenDays(
            TimeSlot timeSlot, List<DayOfWeek> availableOnDays) {
        return availableOnDays.stream()
                .anyMatch(
                        dayOfWeek ->
                                dailySlots
                                        .getOrDefault(dayOfWeek, emptyList())
                                        .contains(new CourtScheduleSlot(court, timeSlot)));
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

    public List<CourtScheduleSlot> getAllSlots() {
        return dailySlots.values().stream().flatMap(Collection::stream).collect(toList());
    }

    public boolean hasScheduleSlot(LocalDate date, TimeSlot timeSlot) {
        return dailySlots.getOrDefault(date.getDayOfWeek(), emptyList()).stream()
                .anyMatch(courtScheduleSlot -> courtScheduleSlot.getTimeSlot().equals(timeSlot));
    }

    public boolean doesNotHaveScheduleSlot(LocalDate date, TimeSlot timeSlot) {
        return !hasScheduleSlot(date, timeSlot);
    }

    boolean isForCourt(Court court) {
        return this.court.equals(court);
    }
}
