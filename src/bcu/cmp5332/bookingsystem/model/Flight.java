package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a flight in the booking system.
 * Manages flight details, passenger list, and dynamic pricing.
 */
public class Flight {
    /** Unique identifier for the flight */
    private int id;

    /** Flight number (airline code + number) */
    private String flightNumber;

    /** Departure airport */
    private String origin;

    /** Arrival airport */
    private String destination;

    /** Date of departure */
    private LocalDate departureDate;

    /** Standard ticket price */
    private double basePrice;

    /** Maximum number of passengers */
    private int capacity;

    /** Flag indicating if the flight has been soft-deleted */
    private boolean isDeleted = false;

    /** Set of passengers booked on this flight */
    private final Set<Customer> passengers;

    /**
     * Constructs a new Flight with the specified details.
     *
     * @param id            unique identifier for the flight
     * @param flightNumber  airline code and flight number
     * @param origin        departure airport
     * @param destination   arrival airport
     * @param departureDate date of departure
     * @param basePrice     standard ticket price
     * @param capacity      maximum number of passengers
     */
    public Flight(int id, String flightNumber, String origin, String destination,
            LocalDate departureDate, double basePrice, int capacity) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureDate = departureDate;
        this.basePrice = basePrice;
        this.capacity = capacity;
        this.passengers = new HashSet<>();
    }

    /** @return the flight's unique identifier */
    public int getId() {
        return id;
    }

    /** @return the flight number */
    public String getFlightNumber() {
        return flightNumber;
    }

    /** @return the departure airport */
    public String getOrigin() {
        return origin;
    }

    /** @return the arrival airport */
    public String getDestination() {
        return destination;
    }

    /** @return the departure date */
    public LocalDate getDepartureDate() {
        return departureDate;
    }

    /** @return the standard ticket price */
    public double getBasePrice() {
        return basePrice;
    }

    /** @return the maximum passenger capacity */
    public int getCapacity() {
        return capacity;
    }

    /** @return true if the flight has been soft-deleted */
    public boolean isDeleted() {
        return isDeleted;
    }

    /** @param deleted true to mark as deleted, false otherwise */
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    /**
     * Calculates the ticket price based on how far in advance the booking is made.
     * - Within 7 days: 50% increase
     * - Within 14 days: 25% increase
     * - More than 14 days: base price
     *
     * @param bookingDate the date when the booking is being made
     * @return the calculated ticket price
     */
    public double calculatePrice(LocalDate bookingDate) {
        double basePrice = this.basePrice;

        // Calculate days until departure
        long daysUntilFlight = ChronoUnit.DAYS.between(bookingDate, departureDate);

        // Calculate seats remaining percentage
        int seatsRemaining = capacity - passengers.size();
        double occupancyRate = 1.0 - ((double) seatsRemaining / capacity);

        // Price adjustments
        double priceMultiplier = 1.0;

        // Adjust based on days until flight
        if (daysUntilFlight <= 7) {
            priceMultiplier += 0.3; // 30% increase for last week
        } else if (daysUntilFlight <= 30) {
            priceMultiplier += 0.15; // 15% increase for last month
        }

        // Adjust based on occupancy
        if (occupancyRate >= 0.8) {
            priceMultiplier += 0.25; // 25% increase when >80% full
        } else if (occupancyRate >= 0.6) {
            priceMultiplier += 0.15; // 15% increase when >60% full
        }

        return basePrice * priceMultiplier;
    }

    /**
     * Adds a passenger to the flight if there is available capacity.
     *
     * @param passenger the customer to add as a passenger
     * @return true if passenger was added successfully, false if flight is full
     */
    public boolean addPassenger(Customer passenger) {
        if (passenger == null) {
            return false;
        }
        // Check if flight is full
        if (passengers.size() >= capacity) {
            return false;
        }
        // Check if passenger is already on this flight
        if (passengers.contains(passenger)) {
            return false;
        }

        boolean added = passengers.add(passenger);

        return added;
    }

    /**
     * Removes a passenger from the flight when their booking is cancelled.
     * 
     * @param customer the customer to remove from the flight
     * @return true if the customer was removed, false if they weren't on the flight
     */
    public boolean removePassenger(Customer customer) {
        return passengers.remove(customer);
    }

    /**
     * @return a defensive copy of the passenger list
     */
    public List<Customer> getPassengers() {
        for (Customer c : passengers) {
            System.out.println("DEBUG: Passenger: " + c.getName());
        }
        return new ArrayList<>(passengers);
    }

    /**
     * Returns a short summary of the flight details.
     *
     * @return a string containing basic flight information
     */
    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " + destination +
                " on " + departureDate.format(dtf) + ", Base Price: $" + basePrice + ", Capacity: " + capacity;
    }

    /**
     * Returns a detailed description of the flight including passenger list.
     *
     * @return a string containing all flight details and passenger information
     */
    public String getDetailsLong() {
        StringBuilder sb = new StringBuilder();
        sb.append("Flight ID: ").append(id)
                .append("\nFlight Number: ").append(flightNumber)
                .append("\nOrigin: ").append(origin)
                .append("\nDestination: ").append(destination)
                .append("\nDeparture Date: ").append(departureDate)
                .append("\nBase Price: $").append(basePrice)
                .append("\nCapacity: ").append(capacity)
                .append("\nPassengers: ");
        if (passengers.isEmpty()) {
            sb.append("None");
        } else {
            for (Customer c : passengers) {
                sb.append(c.getName()).append(" (").append(c.getPhone()).append("), ");
            }
            if (sb.length() >= 2)
                sb.setLength(sb.length() - 2);
        }
        return sb.toString();
    }
}