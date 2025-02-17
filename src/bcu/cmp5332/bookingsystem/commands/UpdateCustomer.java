package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that handles the updating of an existing customer's information.
 * This class implements the Command interface and provides functionality to modify
 * a customer's details such as name, phone, email, and password.
 */
public class UpdateCustomer implements Command {
    /** The unique identifier of the customer to be updated */
    private final int customerId;
    
    /** The new name for the customer */
    private final String name;
    
    /** The new phone number for the customer */
    private final String phone;
    
    /** The new email address for the customer */
    private final String email;
    
    /** The new password for the customer */
    private final String password;
    
    /**
     * Constructs a new UpdateCustomer command with the specified customer details.
     *
     * @param customerId the ID of the customer to be updated
     * @param name      the new name for the customer
     * @param phone     the new phone number for the customer
     * @param email     the new email address for the customer
     * @param password  the new password for the customer
     */
    public UpdateCustomer(int customerId, String name, String phone, String email, String password) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    
    /**
     * Executes the update customer command by modifying the details of
     * the specified customer in the system.
     *
     * @param flightBookingSystem the flight booking system containing the customer to update
     * @throws FlightBookingSystemException if the customer ID is not found in the system
     *         or if there are validation errors with the new details
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        Customer customer = flightBookingSystem.getCustomerByID(customerId);
        customer.setName(name);
        customer.setPhone(phone);
        customer.setEmail(email);
        customer.setPassword(password);
        System.out.println("Customer #" + customerId + " updated.");
    }
}