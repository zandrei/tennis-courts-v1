package com.tenniscourts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

public class CourtScheduler {

  private final HashMap<Court, List<CourtScheduleSlot>> courtScheduleSlots = new HashMap<>();

  CourtScheduler() {}

  public List<CourtScheduleSlot> getFreeScheduleSlots() {
    return emptyList();
  }

  public void createScheduleSlot(Court court, TimeSlot timeSlot) {
    final var courtScheduleSlot = new CourtScheduleSlot(court, timeSlot);
    courtScheduleSlots.putIfAbsent(court, new ArrayList<>());
    courtScheduleSlots.get(court).add(courtScheduleSlot);
  }

  public List<CourtScheduleSlot> getCourtScheduleSlots() {
    return courtScheduleSlots.values().stream().flatMap(Collection::stream).collect(toList());
  }
}
