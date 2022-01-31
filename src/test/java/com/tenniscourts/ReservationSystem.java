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
                .filter(this::courtScheduleSlotIsNotAlreadyBooked)
                .collect(toList());
    }

    private boolean courtScheduleSlotIsNotAlreadyBooked(CourtScheduleSlot courtScheduleSlot) {
        return bookings.stream().noneMatch(booking -> booking.isForScheduleSlot(courtScheduleSlot));
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

        public boolean isForScheduleSlot(CourtScheduleSlot courtScheduleSlot) {
            return court.equals(courtScheduleSlot.getCourt())
                    && timeSlot.equals(courtScheduleSlot.getTimeSlot());
        }
    }
}
