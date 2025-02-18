package bcu.cmp5332.bookingsystem.commands;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.time.LocalDate;

/**
 * Command class that handles the addition of a new flight to the booking system.
 * This class implements the Command interface and provides functionality to create
 * a new flight with specified details such as flight number, origin, destination,
 * departure date, price, and capacity.
 */
public class AddFlight implements Command {
    /** The unique flight number identifier */
    private final String flightNumber;
    
    /** The origin/departure airport */
    private final String origin;
    
    /** The destination airport */
    private final String destination;
    
    /** The date of departure */
    private final LocalDate departureDate;
    
    /** The base ticket price for the flight */
    private final double price;
    
    /** The maximum number of passengers that can be booked */
    private final int capacity;

    /**
     * Constructs a new AddFlight command with the specified flight details.
     *
     * @param flightNumber   the unique identifier for the flight
     * @param origin        the departure airport location
     * @param destination   the arrival airport location
     * @param departureDate the scheduled date of departure
     * @param basePrice     the base ticket price for the flight
     * @param capacity      the maximum number of passengers allowed
     */
    public AddFlight(String flightNumber, String origin, String destination, LocalDate departureDate, double price, int capacity) {
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.price = price;
        this.capacity = capacity;
    }

    /**
     * Executes the add flight command by creating a new flight with an automatically
     * generated ID and adding it to the flight booking system.
     *
     * @param flightBookingSystem the flight booking system where the flight will be added
     * @throws FlightBookingSystemException if there is an error while adding the flight,
     *         such as duplicate flight number or invalid flight details
     */
    @Override
    public void execute(FlightBookingSystem flightBookingSystem) throws FlightBookingSystemException {
        // Debug prints
        System.out.println("Executing AddFlight command with following parameters:");
        System.out.println("Flight Number: " + flightNumber);
        System.out.println("Origin: " + origin);
        System.out.println("Destination: " + destination);
        System.out.println("Departure Date: " + departureDate);
        System.out.println("Price: " + price);
        System.out.println("Capacity: " + capacity);

        // Validate inputs
        if (flightNumber == null || flightNumber.trim().isEmpty()) {
            throw new FlightBookingSystemException("Flight number cannot be empty");
        }
        if (origin == null || origin.trim().isEmpty()) {
            throw new FlightBookingSystemException("Origin cannot be empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new FlightBookingSystemException("Destination cannot be empty");
        }
        if (departureDate == null) {
            throw new FlightBookingSystemException("Departure date cannot be null");
        }
        if (price <= 0) {
            throw new FlightBookingSystemException("Price must be greater than 0");
        }
        if (capacity <= 0) {
            throw new FlightBookingSystemException("Capacity must be greater than 0");
        }

        // Generate new flight ID
        int maxId = flightBookingSystem.getAllFlights().stream()
                .mapToInt(Flight::getId)
                .max()
                .orElse(0);
        int newId = maxId + 1;

        // Create and add the flight
        Flight flight = new Flight(newId, flightNumber, origin, destination, 
                departureDate, price, capacity);
        
        System.out.println("Created new flight with ID: " + newId);
        
        flightBookingSystem.addFlight(flight);
        
        System.out.println("Flight added successfully to the system");
    }
}