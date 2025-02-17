package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.model.Payment;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data manager implementation that handles the persistence of payment data.
 * This class is responsible for loading and storing payment information from/to a text file.
 * It implements the DataManager interface and provides specific functionality for
 * managing payment records associated with bookings.
 */
public class PaymentDataManager implements DataManager {
    
    /** 
     * The file path where payment data is stored.
     * Each payment is stored as a line with fields separated by the SEPARATOR constant.
     */
    private static final String FILENAME = "resources/data/payments.txt";

    /**
     * Loads payment data from the text file into the flight booking system.
     * Each line in the file represents a payment with fields separated by the SEPARATOR.
     * The expected format is: bookingId::amount::cardNumber::expiryDate::paymentDate
     *
     * All fields are required and must be present in the specified format.
     * The payment date must be in ISO format (YYYY-MM-DD).
     *
     * @param fbs the flight booking system to load the payments into
     * @throws IOException if there is an error reading the file
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(FILENAME))) {
            String line = br.readLine();
            while (line != null) {
                String[] parts = line.split("::");
                int bookingId = Integer.parseInt(parts[0]);
                double amount = Double.parseDouble(parts[1]);
                String cardNumber = parts[2];
                String expiryDate = parts[3];
                LocalDate paymentDate = LocalDate.parse(parts[4]);

                Payment payment = new Payment(bookingId, amount, cardNumber, expiryDate, paymentDate);
                fbs.addPayment(payment);
                line = br.readLine();
            }
        }
    }

    /**
     * Stores the current payment data to the text file.
     * Each payment is written as a single line with fields separated by the SEPARATOR.
     * The format is: bookingId::amount::cardNumber::expiryDate::paymentDate
     *
     * All payment records are stored, including those for cancelled bookings.
     * The payment date is stored in ISO format (YYYY-MM-DD).
     *
     * @param fbs the flight booking system containing the payments to store
     * @throws IOException if there is an error writing to the file
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(FILENAME))) {
            for (Payment payment : fbs.getPayments()) {
                out.println(String.format("%d::%f::%s::%s::%s",
                        payment.getBookingId(),
                        payment.getAmount(),
                        payment.getCardNumber(),
                        payment.getExpiryDate(),
                        payment.getPaymentDate()));
            }
        }
    }
}