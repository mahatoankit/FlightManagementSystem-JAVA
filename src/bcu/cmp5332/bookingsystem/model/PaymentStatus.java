package bcu.cmp5332.bookingsystem.model;

/**
 * Represents the possible states of a payment in the flight booking system.
 * This enum defines all valid payment statuses that a booking can have.
 */
public enum PaymentStatus {
    /** Payment has been initiated but not yet processed */
    PENDING,
    
    /** Payment has been successfully processed */
    COMPLETED,
    
    /** Payment processing attempt failed */
    FAILED,
    
    /** Payment was processed but later refunded */
    REFUNDED, PAID
}