package bcu.cmp5332.bookingsystem.payment;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.PaymentStatus;

public class PaymentProcessor {
    public static boolean processPayment(Booking booking, double amountPaid) {
        // If the amount paid is at least the booking fee, payment is successful.
        if (amountPaid >= booking.getBookingFee()) {
            booking.setPaymentStatus(PaymentStatus.COMPLETED);
            return true;
        } else {
            booking.setPaymentStatus(PaymentStatus.FAILED);
            return false;
        }
    }
}
