package bcu.cmp5332.bookingsystem.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class BookingTest {
    private Booking booking;
    private Flight flight;
    private Customer customer;
    private final double BOOKING_FEE = 25.0;
    private LocalDate bookingDate;

    @BeforeEach
    void setUp() {
        bookingDate = LocalDate.now();
        flight = new Flight(1, "BA123", "LHR", "JFK", 
            LocalDate.now().plusDays(30), 500.0, 100);
        customer = new Customer(1, "John Doe", "1234567890", "john@example.com");
        booking = new Booking(1, customer, flight, bookingDate, BOOKING_FEE);
    }

    @Test
    void testBookingConstructorAndGetters() {
        assertAll(
            () -> assertEquals(1, booking.getId()),
            () -> assertEquals(customer, booking.getCustomer()),
            () -> assertEquals(flight, booking.getFlight()),
            () -> assertEquals(bookingDate, booking.getBookingDate()),
            () -> assertEquals(BOOKING_FEE, booking.getBookingFee()),
            () -> assertEquals(PaymentStatus.PENDING, booking.getPaymentStatus()),
            () -> assertFalse(booking.isPaymentProcessed())
        );
    }

    @Test
    void testPaymentStatusUpdate() {
        booking.setPaymentStatus(PaymentStatus.PAID);
        assertEquals(PaymentStatus.PAID, booking.getPaymentStatus());
    }

    @Test
    void testPaymentProcessedUpdate() {
        assertFalse(booking.isPaymentProcessed());
        booking.setPaymentProcessed(true);
        assertTrue(booking.isPaymentProcessed());
    }

    @Test
    void testCancelBooking() {
        booking.cancel();
        assertAll(
            () -> assertTrue(booking.isCancelled()),
            () -> assertFalse(booking.getFlight().getPassengers().contains(booking.getCustomer()))
        );
    }

    @Test
    void testGetFinalPrice() {
        double expectedPrice = flight.calculatePrice(bookingDate);
        assertEquals(expectedPrice, booking.getFinalPrice());
    }

    @Test
    void testToString() {
        String bookingString = booking.toString();
        assertAll(
            () -> assertTrue(bookingString.contains(String.valueOf(booking.getId()))),
            () -> assertTrue(bookingString.contains(customer.getName())),
            () -> assertTrue(bookingString.contains(flight.getFlightNumber())),
            () -> assertTrue(bookingString.contains(bookingDate.toString())),
            () -> assertTrue(bookingString.contains(String.valueOf(BOOKING_FEE)))
        );
    }

    @Test
    void testInitialPaymentStatus() {
        assertEquals(PaymentStatus.PENDING, booking.getPaymentStatus());
    }

    @Test
    void testBookingFee() {
        assertEquals(BOOKING_FEE, booking.getBookingFee());
    }
}