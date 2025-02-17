package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import java.util.ArrayList;
import java.util.List;

public class FlightBookingSystemData {

    private static final List<DataManager> dataManagers = new ArrayList<>();

    static {
        dataManagers.add(new FlightDataManager());
        dataManagers.add(new CustomerDataManager());
        dataManagers.add(new BookingDataManager());
        dataManagers.add(new PaymentDataManager());
    }

    public static FlightBookingSystem load() throws Exception {
        FlightBookingSystem fbs = new FlightBookingSystem();
        for (DataManager dm : dataManagers) {
            dm.loadData(fbs);
        }
        return fbs;
    }

    public static void store(FlightBookingSystem fbs) throws Exception {
        for (DataManager dm : dataManagers) {
            dm.storeData(fbs);
        }
    }
}
