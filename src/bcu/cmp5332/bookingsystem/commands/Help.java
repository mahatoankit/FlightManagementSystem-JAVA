package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

public class Help implements Command {
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
	