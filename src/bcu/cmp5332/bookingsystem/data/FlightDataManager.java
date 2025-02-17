package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.Scanner;

/**
 * Data manager implementation that handles the persistence of flight data.
 * This class is responsible for loading and storing flight information from/to a text file.
 * It implements the DataManager interface and provides specific functionality for
 * managing flight records, including handling soft deletion status.
 */
public class FlightDataManager implements DataManager {
    
    /** 
     * The file path where flight data is stored.
     * Each flight is stored as a line with fields separated by the SEPARATOR constant.
     */
    private final String RESOURCE = "./resources/data/flights.txt";
    
    /**
     * Loads flight data from the text file into the flight booking system.
     * Each line in the file represents a flight with fields separated by the SEPARATOR.
     * The expected format is: id::flightNumber::origin::destination::departureDate::basePrice::capacity::isDeleted
     *
     * Handles special cases:
     * - If basePrice field is empty or missing, sets default price as 100.0
     * - If capacity field is empty or missing, sets default capacity as 150
     * - If isDeleted field is missing or empty, sets it to false
     * - Empty lines are skipped
     *
     * @param fbs the flight booking system to load the flights into
     * @throws IOException if there is an error reading the file
     * @throws FlightBookingSystemException if there is an error parsing the flight data
     */
    @Override
    public void loadData(FlightBookingSystem fbs) throws IOException, FlightBookingSystemException {
        File file = new File(RESOURCE);
        if (!file.exists()) return;
        try (Scanner sc = new Scanner(file)) {
            int line_idx = 1;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if(line.isEmpty()){
                    line_idx++;
                    continue;
                }
                String[] properties = line.split(SEPARATOR, -1);
                try {
                    int id = Integer.parseInt(properties[0]);
                    String flightNumber = properties[1];
                    String origin = properties[2];
                    String destination = properties[3];
                    LocalDate departureDate = LocalDate.parse(properties[4]);
                    double basePrice = properties.length > 5 && !properties[5].isEmpty() ? Double.parseDouble(properties[5]) : 100.0;
                    int capacity = properties.length > 6 && !properties[6].isEmpty() ? Integer.parseInt(properties[6]) : 150;
                    boolean isDeleted = properties.length > 7 && !properties[7].isEmpty() ? Boolean.parseBoolean(properties[7]) : false;
                    
                    Flight flight = new Flight(id, flightNumber, origin, destination, departureDate, basePrice, capacity);
                    flight.setDeleted(isDeleted);
                    fbs.addFlight(flight);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse flight data on line " + line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores the current flight data to the text file.
     * Each flight is written as a single line with fields separated by the SEPARATOR.
     * The format is: id::flightNumber::origin::destination::departureDate::basePrice::capacity::isDeleted
     *
     * All flights in the system are stored, including both active and soft-deleted flights.
     * The isDeleted flag is used to track soft-deleted flights instead of removing them
     * from the file completely.
     *
     * @param fbs the flight booking system containing the flights to store
     * @throws IOException if there is an error writing to the file
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Flight flight : fbs.getAllFlights()) {
                out.print(flight.getId() + SEPARATOR);
                out.print(flight.getFlightNumber() + SEPARATOR);
                out.print(flight.getOrigin() + SEPARATOR);
                out.print(flight.getDestination() + SEPARATOR);
                out.print(flight.getDepartureDate() + SEPARATOR);
                out.print(flight.getBasePrice() + SEPARATOR);
                out.print(flight.getCapacity() + SEPARATOR);
                out.print(flight.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}