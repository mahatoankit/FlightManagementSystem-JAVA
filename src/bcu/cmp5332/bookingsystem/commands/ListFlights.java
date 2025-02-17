package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that displays a list of all flights in the flight booking system.
 * This class implements the Command interface and provides functionality to show
 * a brief summary of each flight's details.
 */
public class ListFlights implements Command {
    
    /**
     * Executes the list flights command by displaying a short summary
     * of all flights registered in the system.
     *
     * @param fbs the flight booking system containing the flights to be listed
     */
    @Override
    public void execute(FlightBookingSystem fbs) {
        fbs.getFlights().forEach(flight -> System.out.println(flight.getDetailsShort()));
    }
}