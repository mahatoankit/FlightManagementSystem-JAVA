package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

/**
 * Represents a booking in the flight booking system.
 * A booking associates a customer with a flight and tracks payment status.
 */
public class Booking {
    /** Unique identifier for the booking */
    private int id;

    /** Customer who made the booking */
    private final Customer customer;

    /** Flight that was booked */
    private final Flight flight;

    /** Date when the booking was made */
    private final LocalDate bookingDate;

    /** Flag indicating if the booking has been cancelled */
    private boolean isCancelled = false;

    /** Fee charged for the booking */
    private double bookingFee;

    /** Current status of the payment (PENDING, PAID, FAILED) */
    private PaymentStatus paymentStatus;

    /** Flag indicating if payment has been processed */
    private boolean paymentProcessed = false;

    private static final String SEPARATOR = "::";

    private final double finalPrice;

    /**
     * Constructs a new Booking with the specified details.
     *
     * @param id          unique identifier for the booking
     * @param customer    customer making the booking
     * @param flight      flight being booked
     * @param bookingDate date when the booking is made
     * @param bookingFee  fee charged for the booking
     */
    public Booking(int id, Customer customer, Flight flight, LocalDate bookingDate, double bookingFee) {
        this.id = id;
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingFee = bookingFee;
        this.paymentStatus = PaymentStatus.PENDING;
        this.finalPrice = flight.calculatePrice(bookingDate);
        // Check if payment exists in payments.txt and set status accordingly
        try {
            java.nio.file.Path path = java.nio.file.Paths.get("resources/data/payments.txt");
            if (java.nio.file.Files.exists(path)) {
                java.util.List<String> lines = java.nio.file.Files.readAllLines(path);
                for (String line : lines) {
                    String[] parts = line.split(SEPARATOR);
                    if (parts.length >= 5 && Integer.parseInt(parts[0]) == this.id) {
                        this.paymentStatus = PaymentStatus.PAID;
                        this.paymentProcessed = true;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            // If there's any error reading the file, keep default PENDING status
            System.err.println("Error checking payment status: " + e.getMessage());
        }
    }

 
    /**
     * @return the booking's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * @return the customer who made the booking
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @return the flight that was booked
     */
    public Flight getFlight() {
        return flight;
    }

    /**
     * @return the date when the booking was made
     */
    public LocalDate getBookingDate() {
        return bookingDate;
    }

    /**
     * @return true if the booking has been cancelled, false otherwise
     */
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * @return the fee charged for the booking
     */
    public double getBookingFee() {
        return bookingFee;
    }

    /**
     * @return the current payment status
     */
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Updates the payment status of the booking.
     *
     * @param paymentStatus the new payment status
     */
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return true if payment has been processed, false otherwise
     */
    public boolean isPaymentProcessed() {
        return paymentProcessed;
    }

    /**
     * Updates the payment processed status.
     *
     * @param paymentProcessed true if payment has been processed, false otherwise
     */
    public void setPaymentProcessed(boolean paymentProcessed) {
        this.paymentProcessed = paymentProcessed;
    }

    /**
     * Cancels the booking by:
     * - Setting the cancelled flag to true
     * - Removing the customer from the flight's passenger list
     * - Removing the booking from the customer's booking list
     */
    public void cancel() {
        isCancelled = true;
        flight.getPassengers().remove(customer);
        customer.cancelBooking(this);
    }

    /**
     * Returns a string representation of the booking.
     *
     * @return a string containing booking ID, customer name, flight number,
     *         booking date, and fee
     */
    @Override
    public String toString() {
        return "Booking #" + id + " for " + customer.getName() + " on flight " + flight.getFlightNumber() +
                " (" + bookingDate + "), Fee: $" + bookingFee;
    }

    /**
     * @return the final price of the booking
     */
    public double getFinalPrice() {
        return finalPrice;
    }
}