package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a payment transaction in the flight booking system.
 * This class stores payment details including card information and amount.
 * All fields are immutable to ensure payment record integrity.
 */
public class Payment {
    /** The ID of the booking this payment is for */
    private final int bookingId;
    
    /** The payment amount */
    private final double amount;
    
    /** The credit card number used for payment */
    private final String cardNumber;
    
    /** The expiry date of the credit card (MM/YY format) */
    private final String expiryDate;
    
    /** The date when the payment was processed */
    private final LocalDate paymentDate;

    /**
     * Constructs a new Payment with the specified details.
     *
     * @param bookingId    the ID of the booking being paid for
     * @param amount       the payment amount
     * @param cardNumber   the credit card number (16 digits)
     * @param expiryDate   the card expiry date (MM/YY format)
     * @param paymentDate  the date when payment was processed
     */
    public Payment(int bookingId, double amount, String cardNumber, String expiryDate, LocalDate paymentDate) {
        this.bookingId = bookingId;
        this.amount = amount;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.paymentDate = paymentDate;
    }

    /** @return the booking ID this payment is for */
    public int getBookingId() { 
        return bookingId; 
    }

    /** @return the payment amount */
    public double getAmount() { 
        return amount; 
    }

    /** @return the credit card number */
    public String getCardNumber() { 
        return cardNumber; 
    }

    /** @return the card expiry date */
    public String getExpiryDate() { 
        return expiryDate; 
    }

    /** @return the date when payment was processed */
    public LocalDate getPaymentDate() { 
        return paymentDate; 
    }

    /**
     * Returns a string representation of the payment.
     * For security, only shows last 4 digits of the card number.
     *
     * @return formatted string with payment details
     */
    @Override
    public String toString() {
        return String.format("Payment{bookingId=%d, amount=%.2f, cardNumber=XXXX-XXXX-XXXX-%s, expiryDate=%s, paymentDate=%s}",
                bookingId, amount, cardNumber.substring(12), expiryDate, paymentDate);
    }
}