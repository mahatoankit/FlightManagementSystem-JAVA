package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that displays detailed information about a specific customer.
 * This class implements the Command interface and provides functionality to show
 * the details of a customer identified by their ID.
 */
public class ShowCustomer implements Command {
    
    /** The unique identifier of the customer to display */
    private final int customerId;
    
    /**
     * Constructs a new ShowCustomer command with the specified customer ID.
     *
     * @param customerId the ID of the customer whose details should be displayed
     */
    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }
    
    /**
     * Executes the show customer command by retrieving and displaying
     * the details of the specified customer.
     *
     * @param fbs the flight booking system containing the customer information
     * @throws FlightBookingSystemException if the customer ID is not found in the system
     */
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Customer customer = fbs.getCustomerByID(customerId);
        System.out.println(customer.getDetailsShort());
    }
}