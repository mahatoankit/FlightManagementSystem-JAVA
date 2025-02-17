package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.util.ArrayList;
import java.util.List;

/**
 * Central coordinator class for data persistence in the flight booking system.
 * This class manages multiple data managers and coordinates the loading and storing
 * of different types of data (flights, customers, bookings, payments) in a specific order.
 */
public class FlightBookingSystemData {

    /**
     * List of data managers responsible for different types of data.
     * The order of managers in this list determines the order of loading/storing operations.
     * Current order:
     * 1. Flights (must be loaded first as bookings depend on flights)
     * 2. Customers (must be loaded before bookings)
     * 3. Bookings (requires both flights and customers to be loaded)
     * 4. Payments (requires bookings to be loaded)
     */
    private static final List<DataManager> dataManagers = new ArrayList<>();

    /**
     * Static initializer that sets up the data managers in the correct order.
     * The order is important as there are dependencies between different types of data.
     */
    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
        dataManagers.add(new PaymentDataManager());
    }

    /**
     * Loads all system data from persistent storage using registered data managers.
     * Data is loaded in the order specified by the dataManagers list to ensure
     * that dependencies are satisfied.
     *
     * @return a new FlightBookingSystem instance populated with the loaded data
     * @throws Exception if there is an error during data loading from any manager
     */
    public static FlightBookingSystem load() throws Exception {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }
        return fbs;
    }

    /**
     * Stores all system data to persistent storage using registered data managers.
     * Data is stored in the order specified by the dataManagers list.
     *
     * @param fbs the flight booking system containing the data to be stored
     * @throws Exception if there is an error during data storage from any manager
     */
    public static void store(FlightBookingSystem fbs) throws Exception {
        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }
    }
}