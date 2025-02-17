package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that displays a list of all customers in the flight booking system.
 * This class implements the Command interface and provides functionality to show
 * a brief summary of each customer's details.
 */
public class ListCustomers implements Command {
    
    /**
     * Executes the list customers command by displaying a short summary
     * of all customers registered in the system.
     *
     * @param fbs the flight booking system containing the customers to be listed
     */
    @Override
    public void execute(FlightBookingSystem fbs) {
        fbs.getCustomers().forEach(customer -> System.out.println(customer.getDetailsShort()));
    }
}