package com.tenniscourts;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class CourtScheduler {

    private final HashMap<Court, List<CourtScheduleSlot>> courtScheduleSlots = new HashMap<>();

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

    public void createScheduleSlot(Court court, TimeSlot timeSlot, List<DayOfWeek> availableForDays) {
    }
}
