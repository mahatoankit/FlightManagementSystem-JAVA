package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class ListFlights implements Command {
    @Override
    public void execute(FlightBookingSystem fbs) {
        fbs.getFlights().forEach(flight -> System.out.println(flight.getDetailsShort()));
    }
}
