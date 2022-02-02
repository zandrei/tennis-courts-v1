package com.tenniscourts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
public class Booking {
    private final Court court;
    private final Player player;
    private final LocalDate bookingDate;
    private final TimeSlot timeSlot;
    private Payment payment;
    private DepositRequest depositRequest;

    public boolean isForDailyScheduleSlot(DailyCourtScheduleSlot dailySlot) {
        return bookingDate.equals(dailySlot.getDay())
                && court.equals(dailySlot.getCourt())
                && timeSlot.equals(dailySlot.getTimeSlot());
    }

    public void makeDeposit(User paidBy, Price depositAmount) {
        if (depositRequested()) {
            payment = new Payment(paidBy, depositAmount, Instant.now());
        }
        else throw new IllegalStateException("No deposit was requested for this booking!");
    }

    private boolean depositRequested() {
        return Objects.nonNull(depositRequest);
    }

    public boolean isDepositPaid() {
        return Objects.nonNull(payment);
    }

    @Value
    public static class User {
        Long id;
    }

    public static class DepositRequest {
        private Price requestedAmount;
    }
}
