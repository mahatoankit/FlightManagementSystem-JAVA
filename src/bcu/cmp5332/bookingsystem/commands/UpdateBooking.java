package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class UpdateBooking implements Command {
    private final int bookingId;
    private final int newFlightId;
    
    public UpdateBooking(int bookingId, int newFlightId) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        var oldBooking = fbs.getBookingByID(bookingId);
        int customerId = oldBooking.getCustomer().getId();
        double cancellationFee = 0.15 * oldBooking.getBookingFee();
        fbs.cancelBooking(bookingId, cancellationFee);
        fbs.addBooking(customerId, newFlightId, oldBooking.getBookingDate());
        System.out.println("Booking updated: " + bookingId + " updated to new flight: " + newFlightId + " with cancellation fee: $" + cancellationFee);
    }
}
