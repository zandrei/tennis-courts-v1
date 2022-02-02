package com.tenniscourts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@EqualsAndHashCode
public class Booking {
    private final Court court;
    private final Player player;
    private final LocalDate bookingDate;
    private final TimeSlot timeSlot;
    private DepositRequest depositRequest;
    private final PaymentRequest paymentRequest;

    Booking(Court court, Player player, LocalDate bookingDate, TimeSlot timeSlot) {
        this.court = court;
        this.player = player;
        this.bookingDate = bookingDate;
        this.timeSlot = timeSlot;
        paymentRequest = new PaymentRequest(court.getPricePerHour());
    }

    public boolean isForDailyScheduleSlot(DailyCourtScheduleSlot dailySlot) {
        return bookingDate.equals(dailySlot.getDay())
                && court.equals(dailySlot.getCourt())
                && timeSlot.equals(dailySlot.getTimeSlot());
    }

    public void makeDeposit(User paidBy, Price depositAmount) {
        if (noDepositWasRequested()) {
            throw new IllegalStateException("No deposit was requested for this booking!");
        }
        if (depositRequest.isPaid()) {
            throw new IllegalStateException("Deposit was already paid for this booking!");
        }
        if (!depositRequest.getRequestedAmount().equals(depositAmount)) {
            throw new PaidAmountDifferentThanRequestedException(
                    "The requested deposit amount is not equal to the amount deposited by the user");
        }
        depositRequest.makePayment(new Payment(paidBy, depositAmount, Instant.now()));
    }

    private boolean depositRequested() {
        return Objects.nonNull(depositRequest);
    }

    private boolean noDepositWasRequested() {
        return !depositRequested();
    }

    public boolean isDepositPaid() {
        if (noDepositWasRequested()) {
            return false;
        }
        return depositRequest.isPaid();
    }

    public void createDepositRequest(Price requestedAmount) {
        depositRequest = new DepositRequest(requestedAmount);
    }

    public boolean isPaid() {
        return paymentRequest.isPaid();
    }

    public void makePayment(User paidBy, Price paymentAmount) {
        Objects.requireNonNull(paymentRequest);
        if (paymentRequest.isPaid()) {
            throw new IllegalStateException("Payment was already done for this booking!");
        }
        if (!paymentRequest.getRequestedAmount().equals(paymentAmount)) {
            throw new PaidAmountDifferentThanRequestedException(
                    "The requested payment amount is not equal to the amount paid by the user");
        }
        paymentRequest.makePayment(new Payment(paidBy, paymentAmount, Instant.now()));
    }

    @Value
    public static class User {
        Long id;
    }

    @Getter
    @RequiredArgsConstructor
    public static class DepositRequest {
        final Price requestedAmount;
        private Payment payment;

        public boolean isPaid() {
            return Objects.nonNull(payment);
        }

        public void makePayment(Payment payment) {
            this.payment = payment;
        }
    }

    public static class PaidAmountDifferentThanRequestedException extends RuntimeException {
        public PaidAmountDifferentThanRequestedException(String message) {
            super(message);
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class PaymentRequest {
        final Price requestedAmount;
        private Payment payment;

        public boolean isPaid() {
            return Objects.nonNull(payment);
        }

        public void makePayment(Payment payment) {
            this.payment = payment;
        }
    }
}
