package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.swing.JOptionPane;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

/**
 * Command class that handles the addition of a new booking to the flight
 * booking system.
 * This class implements the Command interface and provides functionality to
 * create
 * a booking for a customer on a specific flight.
 */
public class AddBooking implements Command {
    private static final Logger LOGGER = Logger.getLogger(AddBooking.class.getName());
    private final int customerId;
    private final int flightId;
    private final LocalDate bookingDate;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Constructs a new AddBooking command with the specified parameters.
     *
     * @param customerId  the ID of the customer making the booking
     * @param flightId    the ID of the flight to be booked
     * @param bookingDate the date when the booking is made
     */
    public AddBooking(int customerId, int flightId, LocalDate bookingDate) {
        this.customerId = customerId;
        this.flightId = flightId;
        this.bookingDate = bookingDate;
    }

    /**
     * Executes the add booking command by creating a new booking in the system.
     *
     * @param flightBookingSystem the flight booking system where the booking will
     *                            be added
     * @throws FlightBookingSystemException if there is an error while adding the
     *                                      booking,
     *                                      such as invalid customer ID, invalid
     *                                      flight ID, or if the flight is full
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        try {
            Customer customer = flightBookingSystem.getCustomerByID(customerId);
            Flight flight = flightBookingSystem.getFlightByID(flightId);

            // Create booking
            Booking booking = flightBookingSystem.addBooking(customerId, flightId, bookingDate);

            // Save immediately to file
            try {
                bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.store(flightBookingSystem);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Failed to save booking data", e);
                throw new FlightBookingSystemException("Unable to save booking: " + e.getMessage());
            }

            // Log the successful booking
            LOGGER.log(Level.INFO, "Booking created successfully - ID: {0}", booking.getId());

            // Show success message with booking details
            String successMessage = String.format(
                    "Booking Confirmed!\n\n" +
                            "Customer: %s\n" +
                            "Flight: %s to %s\n" +
                            "Date: %s\n" +
                            "Booking Reference: %d",
                    customer.getName(),
                    flight.getOrigin(),
                    flight.getDestination(),
                    flight.getDepartureDate().format(DATE_FORMATTER),
                    booking.getId());

            JOptionPane.showMessageDialog(null,
                    successMessage,
                    "Booking Confirmation",
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (FlightBookingSystemException e) {
            LOGGER.log(Level.SEVERE, "Booking failed", e);
            throw e;
        }
    }
}