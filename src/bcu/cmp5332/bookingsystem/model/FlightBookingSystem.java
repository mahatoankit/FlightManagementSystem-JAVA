package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Central class that manages the flight booking system.
 * Handles flights, customers, bookings, and their interactions.
 */
public class FlightBookingSystem {
    /** Current system date for calculating prices and validating bookings */
    private final LocalDate systemDate = LocalDate.now();
    
    /** Map of all customers indexed by their IDs */
    private final Map<Integer, Customer> customers = new TreeMap<>();
    
    /** Map of all flights indexed by their IDs */
    private final Map<Integer, Flight> flights = new TreeMap<>();
    
    /** Map of active bookings indexed by their IDs */
    private final Map<Integer, Booking> bookings = new TreeMap<>();
    
    /** Map of cancelled bookings kept for record-keeping */
    private final Map<Integer, Booking> cancelledBookings = new TreeMap<>();
    
    /** List of all payments processed in the system */
    private final List<Payment> payments = new ArrayList<>();

    /** @return the current system date */
    public LocalDate getSystemDate() {
        return systemDate;
    }

    /**
     * Returns a list of all active flights that haven't departed.
     * @return list of available flights
     */
    public List<Flight> getFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted() && !f.getDepartureDate().isBefore(systemDate))
                .collect(Collectors.toList());
    }
    
    /**
     * @return list of all flights including deleted ones
     */
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }
    
    /**
     * Retrieves a flight by its ID.
     *
     * @param id the flight ID
     * @return the flight if found
     * @throws FlightBookingSystemException if flight doesn't exist or is deleted
     */
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        Flight f = flights.get(id);
        if (f == null || f.isDeleted()) {
            throw new FlightBookingSystemException("No flight with that ID.");
        }
        return f;
    }

    /**
     * Returns a list of all active customers.
     * @return list of non-deleted customers
     */
    public List<Customer> getCustomers() {
        return customers.values().stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
    }
    
    /**
     * @return list of all customers including deleted ones
     */
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    /**
     * Retrieves a customer by their ID.
     *
     * @param id the customer ID
     * @return the customer if found
     * @throws FlightBookingSystemException if customer doesn't exist or is deleted
     */
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        Customer c = customers.get(id);
        if (c == null || c.isDeleted()) {
            throw new FlightBookingSystemException("No customer with that ID.");
        }
        return c;
    }
    
    /**
     * Adds a new flight to the system.
     * Checks for duplicate flight numbers on the same date.
     *
     * @param flight the flight to add
     * @throws FlightBookingSystemException if duplicate ID or flight number exists
     */
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) &&
                existing.getDepartureDate().equals(flight.getDepartureDate())) {
                throw new FlightBookingSystemException("Flight already exists on that date.");
            }
        }
        flights.put(flight.getId(), flight);
    }
    
    /**
     * Adds a new customer to the system.
     * Ensures unique names and email addresses among active customers.
     *
     * @param customer the customer to add
     * @throws FlightBookingSystemException if duplicate name, email, or ID exists
     */
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        for (Customer existing : customers.values()) {
            if (!existing.isDeleted()) {
                if (existing.getName().equals(customer.getName())) {
                    throw new FlightBookingSystemException("Customer name already exists.");
                }
                if (existing.getEmail().equals(customer.getEmail())) {
                    throw new FlightBookingSystemException("Email address already in use.");
                }
            }
        }
        customers.put(customer.getId(), customer);
    }
    
    /** @return defensive copy of all bookings */
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings.values());
    }
    
    /** @return defensive copy of cancelled bookings */
    public List<Booking> getCancelledBookings() {
        return new ArrayList<>(cancelledBookings.values());
    }
    
    /**
     * Creates a new booking for a customer on a flight.
     *
     * @param customerId customer making the booking
     * @param flightId flight to book
     * @param bookingDate date when booking is made
     * @return the created booking
     * @throws FlightBookingSystemException if customer/flight not found or flight full
     */
    public Booking addBooking(int customerId, int flightId, LocalDate bookingDate) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);
        Flight flight = getFlightByID(flightId);
        
        if (!flight.addPassenger(customer)) {
            throw new FlightBookingSystemException("Flight is full.");
        }
        
        double price = flight.calculatePrice(bookingDate);
        int bookingId = bookings.size() + cancelledBookings.size() + 1;
        Booking booking = new Booking(bookingId, customer, flight, bookingDate, price);
        customer.addBooking(booking);
        bookings.put(bookingId, booking);
        return booking;
    }
    
    /**
     * Cancels a booking and processes refund.
     *
     * @param bookingId booking to cancel
     * @param cancellationFee fee to charge for cancellation
     * @throws FlightBookingSystemException if booking not found
     */
    public void cancelBooking(int bookingId, double cancellationFee) throws FlightBookingSystemException {
        Booking booking = getBookingByID(bookingId);
        if (booking.isCancelled()) {
            throw new FlightBookingSystemException("Booking is already cancelled.");
        }
        booking.cancel();
        bookings.remove(bookingId);
        cancelledBookings.put(bookingId, booking);
    }
    
    /**
     * Removes a customer and cancels all their bookings.
     *
     * @param customerId customer to remove
     * @throws FlightBookingSystemException if customer not found
     */
    public void removeCustomer(int customerId) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);
        customer.setDeleted(true);
        for (Booking booking : new ArrayList<>(customer.getBookings())) {
            cancelBooking(booking.getId(), 0);
        }
    }
    
    /**
     * Marks a flight as deleted.
     *
     * @param flightId flight to remove
     * @throws FlightBookingSystemException if flight not found
     */
    public void removeFlight(int flightId) throws FlightBookingSystemException {
        Flight flight = getFlightByID(flightId);
        flight.setDeleted(true);
    }
    
    /**
     * Adds a booking from data import.
     *
     * @param booking the booking to add
     * @throws FlightBookingSystemException if duplicate booking ID
     */
    public void addBookingFromData(Booking booking) throws FlightBookingSystemException {
        if (bookings.containsKey(booking.getId()) || cancelledBookings.containsKey(booking.getId())) {
            throw new FlightBookingSystemException("Duplicate booking ID.");
        }
        if (booking.isCancelled()) {
            cancelledBookings.put(booking.getId(), booking);
        } else {
            bookings.put(booking.getId(), booking);
        }
        booking.getCustomer().addBooking(booking);
    }
    
    /**
     * Retrieves a booking by its ID.
     *
     * @param id the booking ID
     * @return the booking if found
     * @throws FlightBookingSystemException if booking not found
     */
    public Booking getBookingByID(int id) throws FlightBookingSystemException {
        Booking booking = bookings.get(id);
        if (booking == null) {
            booking = cancelledBookings.get(id);
        }
        if (booking == null) {
            throw new FlightBookingSystemException("No booking with that ID.");
        }
        return booking;
    }
    
    /**
     * Adds a payment to the system.
     * @param payment the payment to add
     */
    public void addPayment(Payment payment) {
        payments.add(payment);
    }
    
    /**
     * @return defensive copy of all payments
     */
    public List<Payment> getPayments() {
        return new ArrayList<>(payments);
    }
}