package bcu.cmp5332.bookingsystem.main;

import bcu.cmp5332.bookingsystem.model.Customer;
import java.util.ArrayList;
import java.util.List;

public class FlightBookingSystem {
    private List<Customer> customers;

    public FlightBookingSystem() {
        customers = new ArrayList<>();
    }

    public List<Customer> getCustomers() {
        return new ArrayList<>(customers);
    }

    public Customer getCustomerByID(int id) throws FlightBookingSystemException {
        for (Customer customer : customers) {
            if (customer.getId() == id) {
                return customer;
            }
        }
        throw new FlightBookingSystemException("No customer with ID " + id);
    }

    public void addCustomer(Customer customer) throws FlightBookingSystemException {
        if (customer == null) {
            throw new FlightBookingSystemException("Customer cannot be null");
        }

        // Check for duplicate email
        for (Customer existing : customers) {
            if (existing.getEmail().equals(customer.getEmail())) {
                throw new FlightBookingSystemException(
                        "Customer with email " + customer.getEmail() + " already exists");
            }
        }

        // Check for duplicate ID
        for (Customer existing : customers) {
            if (existing.getId() == customer.getId()) {
                throw new FlightBookingSystemException("Customer with ID " + customer.getId() + " already exists");
            }
        }

        customers.add(customer);
    }
}
