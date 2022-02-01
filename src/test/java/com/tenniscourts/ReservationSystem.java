package com.tenniscourts;

import lombok.Value;

import java.time.LocalDate;
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
        return bookings.stream()
                .filter(booking -> booking.getCourt().equals(court))
                .collect(toList());
    }

    public void bookCourtForPlayerOnDateAtTime(
            Court court, Player player, LocalDate bookingDate, TimeSlot timeSlot) {
        final var courtSchedule =
                courtScheduler
                        .getCourtSchedule(court)
                        .orElseThrow(
                                () ->
                                        new IllegalArgumentException(
                                                "Court schedule is not available on the requested date and timeslot!"));
        if (courtSchedule.doesNotHaveScheduleSlot(bookingDate, timeSlot)) {
            throw new IllegalArgumentException(
                    "Court schedule is not available on the requested date and timeslot!");
        }
        final var booking = new Booking(court, player, bookingDate, timeSlot);
        bookings.add(booking);
    }

    @Value
    public static class Booking {
        Court court;
        Player player;
        LocalDate bookingDate;
        TimeSlot timeSlot;

        public boolean isForScheduleSlot(CourtScheduleSlot courtScheduleSlot) {
            return court.equals(courtScheduleSlot.getCourt())
                    && timeSlot.equals(courtScheduleSlot.getTimeSlot());
        }
    }
}
