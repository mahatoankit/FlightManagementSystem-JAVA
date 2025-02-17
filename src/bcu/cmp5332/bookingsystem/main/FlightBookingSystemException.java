package bcu.cmp5332.bookingsystem.main;

/**
 * Custom exception class for the Flight Booking System.
 * This class extends the standard Java Exception class to provide
 * specific exception handling for the flight booking system operations.
 * 
 * @author Flight Booking System Team
 * @version 1.0
 */
public class FlightBookingSystemException extends Exception {
    
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    
    /**
     * Constructs a new FlightBookingSystemException with the specified error message.
     * 
     * @param message The error message that describes the exception.
     */
    public FlightBookingSystemException(String message) {
        super(message);
    }
}