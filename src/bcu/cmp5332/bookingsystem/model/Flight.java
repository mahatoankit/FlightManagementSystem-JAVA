package bcu.cmp5332.bookingsystem.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Flight {
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDate departureDate;
    private double basePrice;
    private int capacity;
    private boolean isDeleted = false;
    private final Set<Customer> passengers;

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

    // Getters and setters
    public int getId() { return id; }
    public String getFlightNumber() { return flightNumber; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public LocalDate getDepartureDate() { return departureDate; }
    public double getBasePrice() { return basePrice; }
    public int getCapacity() { return capacity; }
    public boolean isDeleted() { return isDeleted; }
    public void setDeleted(boolean deleted) { this.isDeleted = deleted; }

    // Dynamic pricing: if booking is made within 7 days: 50% increase; within 14 days: 25% increase.
    public double calculatePrice(LocalDate bookingDate) {
        long daysLeft = ChronoUnit.DAYS.between(bookingDate, departureDate);
        double price = basePrice;
        if (daysLeft <= 7) {
            price *= 1.50;
        } else if (daysLeft <= 14) {
            price *= 1.25;
        }
        return price;
    }

    public boolean addPassenger(Customer passenger) {
        if (passengers.size() < capacity) {
            return passengers.add(passenger);
        }
        return false;
    }

    public List<Customer> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public String getDetailsShort() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return "Flight #" + id + " - " + flightNumber + " - " + origin + " to " + destination +
               " on " + departureDate.format(dtf) + ", Base Price: $" + basePrice + ", Capacity: " + capacity;
    }

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
            if(sb.length()>=2) sb.setLength(sb.length()-2);
        }
        return sb.toString();
    }
}
