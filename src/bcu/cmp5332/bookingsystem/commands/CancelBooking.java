package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class CancelBooking implements Command {
    private final int bookingId;
    private final double cancellationFee;
    
    public CancelBooking(int bookingId, double cancellationFee) {
        this.bookingId = bookingId;
        this.cancellationFee = cancellationFee;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        fbs.cancelBooking(bookingId, cancellationFee);
        System.out.println("Booking " + bookingId + " cancelled with cancellation fee: $" + cancellationFee);
    }
}
