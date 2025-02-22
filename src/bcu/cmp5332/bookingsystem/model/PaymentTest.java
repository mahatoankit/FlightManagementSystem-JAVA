package bcu.cmp5332.bookingsystem.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

class PaymentTest {
    private Payment payment;
    private final int BOOKING_ID = 1;
    private final double AMOUNT = 500.0;
    private final String CARD_NUMBER = "1234567890123456";
    private final String EXPIRY_DATE = "12/25";
    private final LocalDate PAYMENT_DATE = LocalDate.now();

    @BeforeEach
    void setUp() {
        payment = new Payment(BOOKING_ID, AMOUNT, CARD_NUMBER, EXPIRY_DATE, PAYMENT_DATE);
    }

    @Test
    void testPaymentConstructorAndGetters() {
        assertAll(
            () -> assertEquals(BOOKING_ID, payment.getBookingId()),
            () -> assertEquals(AMOUNT, payment.getAmount()),
            () -> assertEquals(CARD_NUMBER, payment.getCardNumber()),
            () -> assertEquals(EXPIRY_DATE, payment.getExpiryDate()),
            () -> assertEquals(PAYMENT_DATE, payment.getPaymentDate())
        );
    }

    @Test
    void testToString() {
        String paymentString = payment.toString();
        assertAll(
            () -> assertTrue(paymentString.contains(String.valueOf(BOOKING_ID))),
            () -> assertTrue(paymentString.contains(String.valueOf(AMOUNT))),
            () -> assertTrue(paymentString.contains("XXXX-XXXX-XXXX-3456")), // Last 4 digits
            () -> assertTrue(paymentString.contains(EXPIRY_DATE)),
            () -> assertTrue(paymentString.contains(PAYMENT_DATE.toString()))
        );
    }

    @Test
    void testToStringCardNumberMasking() {
        String paymentString = payment.toString();
        // Verify that most of the card number is masked
        assertFalse(paymentString.contains(CARD_NUMBER));
        // Verify that only the last 4 digits are visible
        assertTrue(paymentString.contains(CARD_NUMBER.substring(12)));
    }

    @Test
    void testImmutability() {
        LocalDate originalDate = payment.getPaymentDate();
        LocalDate newDate = originalDate.plusDays(1);
        
        // Verify that the payment date cannot be modified externally
        assertNotSame(newDate, payment.getPaymentDate());
        assertEquals(originalDate, payment.getPaymentDate());
    }

    @Test
    void testConstructorWithZeroAmount() {
        Payment zeroPayment = new Payment(BOOKING_ID, 0.0, CARD_NUMBER, EXPIRY_DATE, PAYMENT_DATE);
        assertEquals(0.0, zeroPayment.getAmount());
    }

    @Test
    void testConstructorWithNegativeAmount() {
        Payment negativePayment = new Payment(BOOKING_ID, -100.0, CARD_NUMBER, EXPIRY_DATE, PAYMENT_DATE);
        assertTrue(negativePayment.getAmount() < 0);
    }
}