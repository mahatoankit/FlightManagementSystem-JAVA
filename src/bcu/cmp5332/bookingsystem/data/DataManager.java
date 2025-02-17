package bcu.cmp5332.bookingsystem.data;

public interface DataManager {
    public static final String SEPARATOR = "::";
    void loadData(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) throws Exception;
    void storeData(bcu.cmp5332.bookingsystem.model.FlightBookingSystem fbs) throws Exception;
}
