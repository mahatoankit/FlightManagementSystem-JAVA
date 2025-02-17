package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that handles the updating of an existing booking to a new flight.
 * This class implements the Command interface and provides functionality to change
 * a booking from one flight to another, applying appropriate cancellation fees.
 */
public class UpdateBooking implements Command {
    
    /** The unique identifier of the booking to be updated */
    private final int bookingId;
    
    /** The unique identifier of the new flight for the booking */
    private final int newFlightId;
    
    /**
     * Constructs a new UpdateBooking command with the specified booking and flight IDs.
     *
     * @param bookingId   the ID of the booking to be updated
     * @param newFlightId the ID of the new flight for the booking
     */
    public UpdateBooking(int bookingId, int newFlightId) {
        this.bookingId = bookingId;
        this.newFlightId = newFlightId;
    }
    
    /**
     * Executes the update booking command by cancelling the existing booking
     * and creating a new booking for the same customer on a different flight.
     * A cancellation fee of 15% of the original booking fee is applied.
     *
     * @param fbs the flight booking system where the update will be processed
     * @throws FlightBookingSystemException if the booking ID or new flight ID is not found,
     *         or if there are issues with the cancellation or rebooking process
     */
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