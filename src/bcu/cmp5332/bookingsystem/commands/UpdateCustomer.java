package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class UpdateCustomer implements Command {
    private final int customerId;
    private final String name;
    private final String phone;
    private final String email;
    private final String password;
    
    public UpdateCustomer(int customerId, String name, String phone, String email, String password) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    
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
