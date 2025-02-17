package bcu.cmp5332.bookingsystem.data;

/**
 * Interface defining the data persistence operations for the flight booking system.
 * Implementations of this interface handle loading and storing data for different
 * entities in the system.
 */
public interface DataManager {
    /** Separator used to split fields in the data files */
    public static final String SEPARATOR = "::";

    /**
     * Loads data from a persistent storage into the flight booking system.
     *
     * @param fbs the flight booking system to load the data into
     * @throws Exception if there is an error reading or parsing the data
     */
    void loadData(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) throws Exception;

    /**
     * Stores the current state of the flight booking system to persistent storage.
     *
     * @param fbs the flight booking system containing the data to be stored
     * @throws Exception if there is an error writing the data
     */
    void storeData(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) throws Exception;
}