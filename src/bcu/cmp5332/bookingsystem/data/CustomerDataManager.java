package bcu.cmp5332.bookingsystem.data;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Data manager implementation that handles the persistence of customer data.
 * This class is responsible for loading and storing customer information from/to a text file.
 * It implements the DataManager interface and provides specific functionality for
 * managing customer records, including handling soft deletion status.
 */
public class CustomerDataManager implements DataManager {
    
    /** 
     * The file path where customer data is stored.
     * Each customer is stored as a line with fields separated by the SEPARATOR constant.
     */
    private final String RESOURCE = "./resources/data/customers.txt";
    
    /**
     * Loads customer data from the text file into the flight booking system.
     * Each line in the file represents a customer with fields separated by the SEPARATOR.
     * The expected format is: id::name::phone::email::password::isDeleted
     *
     * Handles special cases:
     * - If password field is empty, sets default password as "default123"
     * - If isDeleted field is missing or empty, sets it to false
     * - Empty lines are skipped
     *
     * @param fbs the flight booking system to load the customers into
     * @throws IOException if there is an error reading the file
     * @throws FlightBookingSystemException if there is an error parsing the customer data
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
                    String name = properties[1];
                    String phone = properties[2];
                    String email = properties[3];
                    String password = (properties.length > 4 && !properties[4].isEmpty()) ? properties[4] : "default123";
                    boolean isDeleted = properties.length > 5 && !properties[5].isEmpty() ? Boolean.parseBoolean(properties[5]) : false;
                    
                    Customer customer = new Customer(id, name, phone, email, password);
                    customer.setDeleted(isDeleted);
                    fbs.addCustomer(customer);
                } catch (NumberFormatException ex) {
                    throw new FlightBookingSystemException("Unable to parse customer data on line " + line_idx + "\nError: " + ex);
                }
                line_idx++;
            }
        }
    }
    
    /**
     * Stores the current customer data to the text file.
     * Each customer is written as a single line with fields separated by the SEPARATOR.
     * The format is: id::name::phone::email::password::isDeleted
     *
     * All customers in the system are stored, including both active and soft-deleted customers.
     * The isDeleted flag is used to track soft-deleted customers instead of removing them
     * from the file completely.
     *
     * @param fbs the flight booking system containing the customers to store
     * @throws IOException if there is an error writing to the file
     */
    @Override
    public void storeData(FlightBookingSystem fbs) throws IOException {
        try (PrintWriter out = new PrintWriter(new FileWriter(RESOURCE))) {
            for (Customer customer : fbs.getAllCustomers()) {
                out.print(customer.getId() + SEPARATOR);
                out.print(customer.getName() + SEPARATOR);
                out.print(customer.getPhone() + SEPARATOR);
                out.print(customer.getEmail() + SEPARATOR);
                out.print(customer.getPassword() + SEPARATOR);
                out.print(customer.isDeleted() + SEPARATOR);
                out.println();
            }
        }
    }
}