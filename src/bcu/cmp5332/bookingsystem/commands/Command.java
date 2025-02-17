package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Interface defining the command pattern for the flight booking system.
 * All commands that can be executed in the system must implement this interface.
 */
public interface Command {
    /**
     * A help message containing all available commands and their descriptions.
     * This constant provides a user-friendly list of commands that can be executed
     * in the flight booking system.
     */
    public static final String HELP_MESSAGE = "Commands:\n"
        + "\tlistflights                               print all flights\n"
        + "\tlistcustomers                             print all customers\n"
        + "\taddflight                                 add a new flight\n"
        + "\taddcustomer                               add a new customer\n"
        + "\tshowflight [flight id]                    show flight details\n"
        + "\tshowcustomer [customer id]                show customer details\n"
        + "\taddbooking [customer id] [flight id]      add a new booking\n"
        + "\tcancelbooking [booking id]                cancel a booking\n"
        + "\teditbooking [booking id] [flight id]        update a booking\n"
        + "\tloadgui                                   loads the GUI version of the app\n"
        + "\thelp                                      prints this help message\n"
        + "\texit                                      exits the program";
    
    /**
     * Executes the command on the specified flight booking system.
     * Each implementing class should provide its specific implementation
     * of the command execution logic.
     *
     * @param flightBookingSystem the system on which the command will be executed
     * @throws FlightBookingSystemException if there is an error during command execution
     */
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException;
}