package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.commands.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user commands and creates corresponding Command objects for the Flight Booking System.
 * This class is responsible for interpreting user input and converting it into executable commands.
 * 
 * @author Flight Booking System Team
 * @version 1.0
 */
public class CommandParser {

    /**
     * Parses a command line input and returns the corresponding Command object.
     * Supports various commands including adding flights/customers, listing data,
     * managing bookings, and system operations.
     *
     * @param line The command line input to parse
     * @return A Command object corresponding to the input
     * @throws IOException If there is an error reading user input
     * @throws FlightBookingSystemException If the command is invalid or cannot be executed
     */
    public static Command parse(String line) throws IOException, FlightBookingSystemException {
        try {
            String[] parts = line.split(" ", 3);
            String cmd = parts[0];

            if (cmd.equals("addflight")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Flight Number: ");
                String flightNumber = reader.readLine();
                System.out.print("Origin: ");
                String origin = reader.readLine();
                System.out.print("Destination: ");
                String destination = reader.readLine();
                LocalDate departureDate = parseDateWithAttempts(reader);
                System.out.print("Base Price: ");
                double basePrice = Double.parseDouble(reader.readLine());
                System.out.print("Capacity: ");
                int capacity = Integer.parseInt(reader.readLine());
                return new AddFlight(flightNumber, origin, destination, departureDate, basePrice, capacity);
            } else if (cmd.equals("addcustomer")) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Customer Name: ");
                String name = reader.readLine();
                System.out.print("Phone: ");
                String phone = reader.readLine();
                System.out.print("Email: ");
                String email = reader.readLine();
                System.out.print("Password: ");
                String password = reader.readLine();
                return new AddCustomer(name, phone, email,password);
            } else if (cmd.equals("listflights")) {
                return new ListFlights();
            } else if (cmd.equals("listcustomers")) {
                return new ListCustomers();
            } else if (cmd.equals("showflight") && parts.length == 2) {
                int id = Integer.parseInt(parts[1]);
                return new ShowFlight(id);
            } else if (cmd.equals("showcustomer") && parts.length == 2) {
                int id = Integer.parseInt(parts[1]);
                return new ShowCustomer(id);
            } else if (cmd.equals("addbooking") && parts.length == 3) {
                int customerId = Integer.parseInt(parts[1]);
                int flightId = Integer.parseInt(parts[2]);
                return new AddBooking(customerId, flightId, LocalDate.now());
            } else if (cmd.equals("cancelbooking") && parts.length == 2) {
                int bookingId = Integer.parseInt(parts[1]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                System.out.print("Cancellation Fee: ");
                double cancellationFee = Double.parseDouble(reader.readLine());
                return new CancelBooking(bookingId, cancellationFee);
            } else if (cmd.equals("loadgui")) {
                return new LoadGUI();
            } else if (cmd.equals("help")) {
                return new Help();
            }
        } catch (NumberFormatException ex) {
            // fall through to error
        }
        throw new FlightBookingSystemException("Invalid command.");
    }
    
    /**
     * Attempts to parse a date string into a LocalDate object with a specified number of attempts.
     * Prompts the user repeatedly until a valid date is entered or attempts are exhausted.
     *
     * @param br The BufferedReader to read input from
     * @param attempts The number of attempts allowed for parsing the date
     * @return A LocalDate object parsed from user input
     * @throws IOException If there is an error reading user input
     * @throws FlightBookingSystemException If the date cannot be parsed after all attempts
     * @throws IllegalArgumentException If the number of attempts is less than 1
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br, int attempts) throws IOException, FlightBookingSystemException {
        if (attempts < 1) {
            throw new IllegalArgumentException("Number of attempts should be higher than 0");
        }
        while (attempts > 0) {
            attempts--;
            System.out.print("Departure Date (YYYY-MM-DD): ");
            try {
                LocalDate departureDate = LocalDate.parse(br.readLine());
                return departureDate;
            } catch (DateTimeParseException dtpe) {
                System.out.println("Date must be in YYYY-MM-DD format. " + attempts + " attempts remaining...");
            }
        }
        throw new FlightBookingSystemException("Incorrect departure date provided. Cannot create flight.");
    }
    
    /**
     * Attempts to parse a date string into a LocalDate object with 3 default attempts.
     *
     * @param br The BufferedReader to read input from
     * @return A LocalDate object parsed from user input
     * @throws IOException If there is an error reading user input
     * @throws FlightBookingSystemException If the date cannot be parsed after all attempts
     */
    private static LocalDate parseDateWithAttempts(BufferedReader br) throws IOException, FlightBookingSystemException {
        return parseDateWithAttempts(br, 3);
    }
}