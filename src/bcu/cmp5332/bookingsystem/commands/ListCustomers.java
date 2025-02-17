package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListCustomers implements Command {
    @Override
    public void execute(FlightBookingSystem fbs) {
        fbs.getCustomers().forEach(customer -> System.out.println(customer.getDetailsShort()));
    }
}
