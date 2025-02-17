package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that displays the help information for the flight booking system.
 * This class implements the Command interface and provides functionality to show
 * all available commands and their usage to the user.
 */
public class Help implements Command {
    
    /**
     * Executes the help command by displaying a list of all available commands
     * and their basic usage information to the console.
     *
     * @param flightBookingSystem the flight booking system (not used in this command)
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) {
        System.out.println("Commands:\n" +
                "\taddflight\n" +
                "\taddcustomer\n" +
                "\tlistflights\n" +
                "\tlistcustomers\n" +
                "\tshowflight [id]\n" +
                "\tshowcustomer [id]\n" +
                "\taddbooking [customerId] [flightId]\n" +
                "\tcancelbooking [bookingId]\n" +
                "\tloadgui\n" +
                "\thelp\n" +
                "\texit");
    }
}