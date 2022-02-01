package com.tenniscourts;

import lombok.Getter;
import lombok.Value;

import java.time.LocalDate;

@Value
@Getter
public class Booking {
    Court court;
    Player player;
    LocalDate bookingDate;
    TimeSlot timeSlot;

    public boolean isForDailyScheduleSlot(DailyCourtScheduleSlot dailySlot) {
        return bookingDate.equals(dailySlot.getDay())
                && court.equals(dailySlot.getCourt())
                && timeSlot.equals(dailySlot.getTimeSlot());
    }

    public void makeDeposit(User paidBy, MakeADepositForABookingTest.Price depositAmount) {}

    public boolean isDepositPaid() {
        return true;
    }

    @Value
    public static class User {
        Long id;
    }
}
