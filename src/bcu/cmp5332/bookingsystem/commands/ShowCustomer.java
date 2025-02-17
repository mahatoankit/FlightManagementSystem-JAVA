package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ShowCustomer implements Command {
    private final int customerId;
    
    public ShowCustomer(int customerId) {
        this.customerId = customerId;
    }
    
    @Override
    public void execute(FlightBookingSystem fbs) throws FlightBookingSystemException {
        Customer customer = fbs.getCustomerByID(customerId);
        System.out.println(customer.getDetailsShort());
    }
}
