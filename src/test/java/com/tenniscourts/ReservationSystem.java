package com.tenniscourts;

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

    public List<DailyCourtScheduleSlot> getFreeDailyScheduleSlots(LocalDate start, LocalDate end) {
        final var dailyScheduleSlots = courtScheduler.getDailyScheduleSlots(start, end);

        return dailyScheduleSlots.stream()
                .filter(this::dailyCourtScheduleSlotIsNotAlreadyBooked)
                .collect(toList());
    }

    private boolean dailyCourtScheduleSlotIsNotAlreadyBooked(DailyCourtScheduleSlot dailySlot) {
        return bookings.stream().noneMatch(booking -> booking.isForDailyScheduleSlot(dailySlot));
    }

    public List<Booking> getAllBookingsForCourt(Court court) {
        return bookings.stream()
                .filter(booking -> booking.getCourt().equals(court))
                .collect(toList());
    }

    public Booking bookCourtForPlayerOnDateAtTime(
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
        return booking;
    }

}
