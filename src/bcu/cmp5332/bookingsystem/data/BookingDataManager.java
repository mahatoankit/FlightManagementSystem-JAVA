package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.payment.PaymentProcessor;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Data manager implementation that handles the persistence of booking data.
 * This class is responsible for loading and storing booking information from/to
 * a text file.
 * It implements the DataManager interface and provides specific functionality
 * for
 * managing booking records, including handling cases where referenced customers
 * or
 * flights may not exist in the system.
 */
public class BookingDataManager implements DataManager {

    /**
     * The file path where booking data is stored.
     * Each booking is stored as a line with fields separated by the SEPARATOR
     * constant.
     */
    private final String RESOURCE = "./resources/data/bookings.txt";

    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        try (BufferedReader reader = new BufferedReader(new FileReader(RESOURCE))) {
            String line;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                line = line.trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(SEPARATOR);
                if (parts.length < 5) {
                    System.err.println("Warning: Invalid data format at line " + lineNumber + ": " + line);
                    continue;
                }

                try {
                    int bookingId = Integer.parseInt(parts[0]);
                    int customerId = Integer.parseInt(parts[1]);
                    int flightId = Integer.parseInt(parts[2]);
                    LocalDate bookingDate = LocalDate.parse(parts[3]);

                    // Instead of creating a Booking object directly, use the FlightBookingSystem's
                    // addBooking method
                    try {
                        Booking booking = fbs.addBooking(customerId, flightId, bookingDate);
                        // If you need to set additional properties like price, do it after creation
                        // booking.setPrice(Double.parseDouble(parts[4]));
                    } catch (FlightBookingSystemException e) {
                        System.err.println(
                                "Warning: Could not create booking at line " + lineNumber + ": " + e.getMessage());
                    }

                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Warning: Invalid data format at line " + lineNumber + ": " + e.getMessage());
                }
            }
        }
    }

    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Booking booking : fbs.getBookings()) {
                out.printf("%d%s%d%s%d%s%s%s%.2f%n",
                        booking.getId(),
                        SEPARATOR,
                        booking.getCustomer().getId(),
                        SEPARATOR,
                        booking.getFlight().getId(),
                        SEPARATOR,
                        booking.getBookingDate(),
                        SEPARATOR,
                        booking.getFinalPrice());
            }
        }
    }

    // Additional method without @Override annotation
    public void storeDataToFile(FlightBookingSystem fbs, String filename) throws IOException {
        try (PrintWriter out = new PrintWriter(new File(filename))) {
            // Store active bookings
            for (Booking booking : fbs.getBookings()) {
                out.printf("%d::%d::%d::%s::%.2f::%b%n",
                        booking.getId(),
                        booking.getCustomer().getId(),
                        booking.getFlight().getId(),
                        booking.getBookingDate(),
                        booking.getBookingFee(),
                        booking.isPaymentProcessed());
            }
            // Store cancelled bookings
            for (Booking booking : fbs.getCancelledBookings()) {
                out.printf("%d::%d::%d::%s::%.2f::%b::cancelled%n",
                        booking.getId(),
                        booking.getCustomer().getId(),
                        booking.getFlight().getId(),
                        booking.getBookingDate(),
                        booking.getBookingFee(),
                        booking.isPaymentProcessed());
            }
        }
    }
}