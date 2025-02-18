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
import java.time.LocalDate;
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
        File file = new File(RESOURCE);
        if (!file.exists()) {
            return;
        }

        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty())
                    continue;

                String[] parts = line.split(SEPARATOR);
                int bookingId = Integer.parseInt(parts[0]);
                int customerId = Integer.parseInt(parts[1]);
                int flightId = Integer.parseInt(parts[2]);
                LocalDate bookingDate = LocalDate.parse(parts[3]);
                double fee = Double.parseDouble(parts[4]);
                boolean paymentStatus = Boolean.parseBoolean(parts[5]);
                boolean isCancelled = parts.length > 6 && parts[6].equals("cancelled");

                Customer customer = fbs.getCustomerByID(customerId);
                Flight flight = fbs.getFlightByID(flightId);

                Booking booking = new Booking(bookingId, customer, flight, bookingDate, fee);

                // Process payment if payment status is true
                if (paymentStatus) {
                    PaymentProcessor.processPayment(booking, fee);
                }

                if (isCancelled) {
                    booking.cancel();
                }

                // Add booking to the system
                fbs.addBookingFromData(booking);

                // Add passenger to flight only for active, non-cancelled bookings
                if (!isCancelled) {
                    flight.addPassenger(customer);
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