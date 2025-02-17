package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

/**
 * Command class that handles the addition of a new customer to the flight booking system.
 * This class implements the Command interface and provides functionality to create
 * a new customer with basic details like name, phone, email, and password.
 */
public class AddCustomer implements Command {
    
    /** The full name of the customer */
    private final String name;
    
    /** The phone number of the customer */
    private final String phone;
    
    /** The email address of the customer */
    private final String email;
    
    /** The login password for the customer */
    private final String password;
    
    /**
     * Constructs a new AddCustomer command with the specified customer details.
     *
     * @param name     the full name of the customer
     * @param phone    the phone number of the customer
     * @param email    the email address of the customer
     * @param password the login password for the customer
     */
    public AddCustomer(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    
    /**
     * Executes the add customer command by creating a new customer with an automatically
     * generated ID and adding them to the flight booking system.
     *
     * @param flightBookingSystem the flight booking system where the customer will be added
     * @throws FlightBookingSystemException if there is an error while adding the customer,
     *         such as duplicate email or invalid customer details
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // ...existing code...
    }
}