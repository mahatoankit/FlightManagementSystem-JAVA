package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command class that handles the addition of a new booking to the flight booking system.
 * This class implements the Command interface and provides functionality to create
 * a booking for a customer on a specific flight.
 */
public class AddBooking implements Command {
    private final int customerId;
    private final int flightId;
    private final LocalDate bookingDate;

    /**
     * Constructs a new AddBooking command with the specified parameters.
     *
     * @param customerId   the ID of the customer making the booking
     * @param flightId     the ID of the flight to be booked
     * @param bookingDate  the date when the booking is made
     */
    public AddBooking(int customerId, int flightId, LocalDate bookingDate) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
    }

    /**
     * Executes the add booking command by creating a new booking in the system.
     *
     * @param flightBookingSystem  the flight booking system where the booking will be added
     * @throws FlightBookingSystemException if there is an error while adding the booking,
     *         such as invalid customer ID, invalid flight ID, or if the flight is full
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Booking booking = flightBookingSystem.addBooking(customerId, flightId, bookingDate);
        System.out.println("Booking added: " + booking);
    }
}