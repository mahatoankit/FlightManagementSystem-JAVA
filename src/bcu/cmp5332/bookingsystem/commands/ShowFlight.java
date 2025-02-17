package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that displays detailed information about a specific flight.
 * This class implements the Command interface and provides functionality to show
 * the detailed information of a flight identified by its ID.
 */
public class ShowFlight implements Command {
    
    /** The unique identifier of the flight to display */
    private final int flightId;
    
    /**
     * Constructs a new ShowFlight command with the specified flight ID.
     *
     * @param flightId the ID of the flight whose details should be displayed
     */
    public ShowFlight(int flightId) {
        this.flightId = flightId;
    }
    
    /**
     * Executes the show flight command by retrieving and displaying
     * the detailed information of the specified flight.
     *
     * @param fbs the flight booking system containing the flight information
     * @throws FlightBookingSystemException if the flight ID is not found in the system
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Flight flight = fbs.getFlightByID(flightId);
        System.out.println(flight.getDetailsLong());
    }
}