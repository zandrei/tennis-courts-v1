package com.tenniscourts;

import lombok.Value;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ReservationSystem {

    private final CourtScheduler courtScheduler;
    private final ArrayList<Booking> bookings;

    public ReservationSystem(CourtScheduler courtScheduler) {
        this.courtScheduler = courtScheduler;
        bookings = new ArrayList<>();
    }

    public List<CourtScheduleSlot> getFreeScheduleSlots() {
        final var courtScheduleSlots = courtScheduler.getCourtScheduleSlots();
        return courtScheduleSlots.stream()
                .filter(
                        courtScheduleSlot ->
                                bookings.stream()
                                        .noneMatch(
                                                booking ->
                                                        booking.getCourt()
                                                                        .equals(
                                                                                courtScheduleSlot
                                                                                        .getCourt())
                                                                && booking.getTimeSlot()
                                                                        .equals(
                                                                                courtScheduleSlot
                                                                                        .getTimeSlot())))
                .collect(toList());
    }

    public List<Booking> getAllBookingsForCourt(Court court) {
        return bookings;
    }

    public void bookCourtForPlayerAtTime(Court court, Player player, TimeSlot timeSlot) {
        final var booking = new Booking(court, player, timeSlot);
        bookings.add(booking);
    }

    @Value
    public static class Booking {
        Court court;
        Player player;
        TimeSlot timeSlot;
    }
}
