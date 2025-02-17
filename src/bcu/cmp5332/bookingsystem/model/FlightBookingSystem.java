package bcu.cmp5332.bookingsystem.model;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FlightBookingSystem {
    private final LocalDate systemDate = LocalDate.now();
    private final Map<Integer, Customer> customers = new TreeMap<>();
    private final Map<Integer, Flight> flights = new TreeMap<>();
    private final Map<Integer, Booking> bookings = new TreeMap<>();
    // Map for cancelled bookings (kept separately)
    private final Map<Integer, Booking> cancelledBookings = new TreeMap<>();

    public LocalDate getSystemDate() { return systemDate; }

    public List<Flight> getFlights() {
        return flights.values().stream()
                .filter(f -> !f.isDeleted() && !f.getDepartureDate().isBefore(systemDate))
                .collect(Collectors.toList());
    }
    
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights.values());
    }
    
    public Flight getFlightByID(int id) throws FlightBookingSystemException {
        Flight f = flights.get(id);
        if (f == null || f.isDeleted()) {
            throw new FlightBookingSystemException("No flight with that ID.");
        }
        return f;
    }
    
    public List<Customer> getCustomers() {
        return customers.values().stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
    }
    
    public List<Customer> getAllCustomers() {
        return new ArrayList<>(customers.values());
    }
    
    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        Customer c = customers.get(id);
        if (c == null || c.isDeleted()) {
            throw new FlightBookingSystemException("No customer with that ID.");
        }
        return c;
    }
    
    public void addFlight(Flight flight) throws FlightBookingSystemException {
        if (flights.containsKey(flight.getId())) {
            throw new FlightBookingSystemException("Duplicate flight ID.");
        }
        for (Flight existing : flights.values()) {
            if (existing.getFlightNumber().equals(flight.getFlightNumber()) &&
                existing.getDepartureDate().isEqual(flight.getDepartureDate()) &&
                !existing.isDeleted()) {
                throw new FlightBookingSystemException("A flight with the same number and date exists.");
            }
        }
        flights.put(flight.getId(), flight);
    }
    
    // Uniqueness check: no active customer may share the same name or email.
    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        for (Customer c : customers.values()) {
            if (!c.isDeleted()) {
                if (c.getName().equalsIgnoreCase(customer.getName())) {
                    throw new FlightBookingSystemException("Customer name already exists.");
                }
                if (c.getEmail().equalsIgnoreCase(customer.getEmail())) {
                    throw new FlightBookingSystemException("Customer email already exists.");
                }
            }
        }
        if (customers.containsKey(customer.getId())) {
            throw new FlightBookingSystemException("Duplicate customer ID.");
        }
        customers.put(customer.getId(), customer);
    }
    
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings.values());
    }
    
    public List<Booking> getCancelledBookings() {
        return new ArrayList<>(cancelledBookings.values());
    }
    
    public Booking addBooking(int customerId, int flightId, LocalDate bookingDate) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);
        Flight flight = getFlightByID(flightId);
        if (flight.getPassengers().size() >= flight.getCapacity()) {
            throw new FlightBookingSystemException("Flight is at full capacity.");
        }
        int bookingMaxId = bookings.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        int newId = bookingMaxId + 1;
        double fee = flight.calculatePrice(bookingDate);
        Booking booking = new Booking(newId, customer, flight, bookingDate, fee);
        bookings.put(newId, booking);
        customer.addBooking(booking);
        if (!flight.addPassenger(customer)) {
            throw new FlightBookingSystemException("Failed to add passenger due to capacity issues.");
        }
        return booking;
    }
    
    // Cancel a booking and mark it as cancelled (store in cancelledBookings).
    public void cancelBooking(int bookingId, double cancellationFee) throws FlightBookingSystemException {
        if (!bookings.containsKey(bookingId)) {
            throw new FlightBookingSystemException("No booking with that ID.");
        }
        Booking booking = bookings.get(bookingId);
        double refund = booking.getBookingFee() - cancellationFee;
        if (refund < 0) refund = 0;
        System.out.println("Refund amount: $" + refund);
        booking.cancel();
        bookings.remove(bookingId);
        cancelledBookings.put(bookingId, booking);
    }
    
    // When a customer is removed, cancel all of their bookings (with 0 fee) so that flight capacity is restored.
    public void removeCustomer(int customerId) throws FlightBookingSystemException {
        Customer customer = getCustomerByID(customerId);
        List<Booking> customerBookings = new ArrayList<>(customer.getBookings());
        for (Booking booking : customerBookings) {
            cancelBooking(booking.getId(), 0.0);
        }
        customer.setDeleted(true);
    }
    
    public void removeFlight(int flightId) throws FlightBookingSystemException {
        Flight flight = getFlightByID(flightId);
        flight.setDeleted(true);
    }
    
    public void addBookingFromData(Booking booking) throws FlightBookingSystemException {
        if (bookings.containsKey(booking.getId())) {
            throw new FlightBookingSystemException("Duplicate booking ID in data.");
        }
        bookings.put(booking.getId(), booking);
        booking.getCustomer().addBooking(booking);
        booking.getFlight().addPassenger(booking.getCustomer());
    }
    
    public Booking getBookingByID(int id) throws FlightBookingSystemException {
        if (!bookings.containsKey(id)) {
            throw new FlightBookingSystemException("No booking with that ID.");
        }
        return bookings.get(id);
    }
    private final List<Payment> payments = new ArrayList<>();

public void addPayment(Payment payment) {
    payments.add(payment);
}

public List<Payment> getPayments() {
    return new ArrayList<>(payments);
}
}
