package bcu.cmp5332.bookingsystem.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

class FlightTest {
    private Flight flight;
    private Customer customer;

    @BeforeEach
    void setUp() {
        // Initialize test objects
        flight = new Flight(1, "BA123", "LHR", "JFK",
                LocalDate.now().plusDays(10), 500.0, 100);
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
    }

    @Test
    void testFlightConstructor() {
        assertAll(
                () -> assertEquals(1, flight.getId()),
                () -> assertEquals("BA123", flight.getFlightNumber()),
                () -> assertEquals("LHR", flight.getOrigin()),
                () -> assertEquals("JFK", flight.getDestination()),
                () -> assertEquals(500.0, flight.getBasePrice()),
                () -> assertEquals(100, flight.getCapacity()),
                () -> assertFalse(flight.isDeleted()));
    }

    @Test
    void testAddPassenger() {
        assertTrue(flight.addPassenger(customer));
        assertTrue(flight.getPassengers().contains(customer));
    }

    @Test
    void testAddDuplicatePassenger() {
        flight.addPassenger(customer);
        assertFalse(flight.addPassenger(customer));
    }

    @Test
    void testAddPassengerWhenFull() {
        // Create a flight with capacity 1
        Flight smallFlight = new Flight(2, "BA456", "LHR", "JFK",
                LocalDate.now().plusDays(10), 500.0, 1);

        Customer customer1 = new Customer(1, "John Doe", "1234567890", "john@example.com");
        Customer customer2 = new Customer(2, "Jane Doe", "0987654321", "jane@example.com");

        assertTrue(smallFlight.addPassenger(customer1));
        assertFalse(smallFlight.addPassenger(customer2));
    }

    @Test
    void testRemovePassenger() {
        flight.addPassenger(customer);
        assertTrue(flight.removePassenger(customer));
        assertFalse(flight.getPassengers().contains(customer));
    }

    @Test
    void testCalculatePrice() {
        LocalDate bookingDate = LocalDate.now();

        // Test booking within 7 days
        Flight nearFlight = new Flight(3, "BA789", "LHR", "JFK",
                bookingDate.plusDays(5), 500.0, 100);
        assertTrue(nearFlight.calculatePrice(bookingDate) > 500.0);

        // Test booking more than 30 days ahead
        Flight farFlight = new Flight(4, "BA012", "LHR", "JFK",
                bookingDate.plusDays(40), 500.0, 100);
        assertEquals(500.0, farFlight.calculatePrice(bookingDate));
    }

    @Test
    void testGetDetailsShort() {
        String details = flight.getDetailsShort();
        assertAll(
                () -> assertTrue(details.contains("BA123")),
                () -> assertTrue(details.contains("LHR")),
                () -> assertTrue(details.contains("JFK")));
    }

    @Test
    void testGetDetailsLong() {
        flight.addPassenger(customer);
        String details = flight.getDetailsLong();
        assertAll(
                () -> assertTrue(details.contains("BA123")),
                () -> assertTrue(details.contains("LHR")),
                () -> assertTrue(details.contains("JFK")),
                () -> assertTrue(details.contains("John Doe")));
    }

    @Test
    void testIsDeleted() {
        assertFalse(flight.isDeleted());
        flight.setDeleted(true);
        assertTrue(flight.isDeleted());
    }
}