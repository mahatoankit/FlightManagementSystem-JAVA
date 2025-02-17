package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class AddBooking implements Command {
    private final int customerId;
    private final int flightId;
    private final LocalDate bookingDate;

    public AddBooking(int customerId, int flightId, LocalDate bookingDate) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Booking booking = flightBookingSystem.addBooking(customerId, flightId, bookingDate);
        System.out.println("Booking added: " + booking);
    }
}
