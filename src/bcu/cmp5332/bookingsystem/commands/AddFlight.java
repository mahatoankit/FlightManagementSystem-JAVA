package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

public class AddFlight implements Command {
    private final String flightNumber;
    private final String origin;
    private final String destination;
    private final LocalDate departureDate;
    private final double basePrice;
    private final int capacity;

    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, double basePrice, int capacity) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.capacity = capacity;
    }

    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        int maxId = flightBookingSystem.getAllFlights().stream().mapToInt(Flight::getId).max().orElse(0);
        Flight flight = new Flight(maxId + 1, flightNumber, origin, destination, departureDate, basePrice, capacity);
        flightBookingSystem.addFlight(flight);
        System.out.println("Flight #" + flight.getId() + " added.");
    }
}
