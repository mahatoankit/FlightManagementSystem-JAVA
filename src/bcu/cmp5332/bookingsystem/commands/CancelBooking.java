package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that handles the cancellation of an existing booking in the flight booking system.
 * This class implements the Command interface and provides functionality to cancel
 * a booking with an associated cancellation fee.
 */
public class CancelBooking implements Command {
    /** The unique identifier of the booking to be cancelled */
    private final int bookingId;
    
    /** The fee charged for cancelling the booking */
    private final double cancellationFee;
    
    /**
     * Constructs a new CancelBooking command with the specified booking details.
     *
     * @param bookingId       the ID of the booking to be cancelled
     * @param cancellationFee the fee to be charged for cancellation
     */
    public CancelBooking(int bookingId, double cancellationFee) {
        this.bookingId = bookingId;
        this.cancellationFee = cancellationFee;
    }
    
    /**
     * Executes the cancel booking command by removing the specified booking from the system
     * and applying the cancellation fee.
     *
     * @param fbs the flight booking system where the cancellation will be processed
     * @throws FlightBookingSystemException if there is an error while cancelling the booking,
     *         such as invalid booking ID or booking already cancelled
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        fbs.cancelBooking(bookingId, cancellationFee);
        System.out.println("Booking " + bookingId + " cancelled with cancellation fee: $" + cancellationFee);
    }
}