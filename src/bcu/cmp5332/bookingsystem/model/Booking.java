package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;

public class Booking {
    private int id;
    private final Customer customer;
    private final Flight flight;
    private final LocalDate bookingDate;
    private boolean isCancelled = false;
    private double bookingFee;
    private PaymentStatus paymentStatus;
    private boolean paymentProcessed = false;

    public Booking(int id, Customer customer, Flight flight, LocalDate bookingDate, double bookingFee) {
        this.id = id;
        this.customer = customer;
        this.flight = flight;
        this.bookingDate = bookingDate;
        this.bookingFee = bookingFee;
        this.paymentStatus = PaymentStatus.PENDING;
    }

    public int getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Flight getFlight() {
        return flight;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public double getBookingFee() {
        return bookingFee;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isPaymentProcessed() {
        return paymentProcessed;
    }

    public void setPaymentProcessed(boolean paymentProcessed) {
        this.paymentProcessed = paymentProcessed;
    }

    public void cancel() {
        isCancelled = true;
        flight.getPassengers().remove(customer);
        customer.cancelBooking(this);
    }

    @Override
    public String toString() {
        return "Booking #" + id + " for " + customer.getName() + " on flight " + flight.getFlightNumber() +
                " (" + bookingDate + "), Fee: $" + bookingFee;
    }
}
