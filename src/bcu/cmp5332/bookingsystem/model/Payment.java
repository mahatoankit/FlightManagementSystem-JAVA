package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Payment {
    private final int bookingId;
    private final double amount;
    private final String cardNumber;
    private final String expiryDate;
    private final LocalDate paymentDate;

    public Payment(int bookingId, double amount, String cardNumber, String expiryDate, LocalDate paymentDate) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.paymentDate = paymentDate;
    }

    // Getters
    public int getBookingId() { return bookingId; }
    public double getAmount() { return amount; }
    public String getCardNumber() { return cardNumber; }
    public String getExpiryDate() { return expiryDate; }
    public LocalDate getPaymentDate() { return paymentDate; }

    @Override
    public String toString() {
        return String.format("Payment{bookingId=%d, amount=%.2f, cardNumber=XXXX-XXXX-XXXX-%s, expiryDate=%s, paymentDate=%s}",
                bookingId, amount, cardNumber.substring(12), expiryDate, paymentDate);
    }
}