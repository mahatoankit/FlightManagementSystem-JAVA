package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class AddCustomer implements Command {
    private final String name;
    private final String phone;
    private final String email;
    private final String password;
    
    public AddCustomer(String name, String phone, String email, String password) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
    }
    
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = flightBookingSystem.getAllCustomers().stream().mapToInt(Customer::getId).max().orElse(0);
        Customer customer = new Customer(maxId + 1, name, phone, email, password);
        flightBookingSystem.addCustomer(customer);
        System.out.println("Customer #" + customer.getId() + " added.");
    }
}
