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

public class FlightDataManager implements DataManager {
    private final String RESOURCE = "./resources/data/flights.txt";
    
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
