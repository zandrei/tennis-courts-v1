package com.tenniscourts;

import lombok.Getter;
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

    public boolean isDepositPayed(Booking booking) {
        return false;
    }

    public MakeADepositForABookingTest.Price getDeposit(Booking booking) {
        return null;
    }

    public void makeDeposit(Player player, Booking booking, MakeADepositForABookingTest.Price depositValue) {

    }

    @Value
    @Getter
    public static class Booking {
        Court court;
        Player player;
        LocalDate bookingDate;
        TimeSlot timeSlot;

        public boolean isForScheduleSlot(CourtScheduleSlot courtScheduleSlot) {
            return court.equals(courtScheduleSlot.getCourt())
                    && timeSlot.equals(courtScheduleSlot.getTimeSlot());
        }

        public boolean isForDailyScheduleSlot(DailyCourtScheduleSlot dailySlot) {
            return bookingDate.equals(dailySlot.getDay())
                    && court.equals(dailySlot.getCourt())
                    && timeSlot.equals(dailySlot.getTimeSlot());
        }

        public void makeDeposit(Player paidBy, MakeADepositForABookingTest.Price depositAmount) {

        }

        public boolean isDepositPaid() {
            return true;
        }
    }
}
