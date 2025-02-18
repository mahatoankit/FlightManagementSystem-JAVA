package bcu.cmp5332.bookingsystem.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a customer in the flight booking system.
 * Maintains customer details and their booking history.
 */
public class Customer {
    /** Unique identifier for the customer */
    private int id;

    /** Customer's full name */
    private String name;

    /** Customer's phone number */
    private String phone;

    /** Customer's email address */
    private String email;

    /** Customer's login password */
    private String password;

    /** List of bookings made by this customer */
    private final List<Booking> bookings;

    /** Flag indicating if the customer has been soft-deleted */
    private boolean isDeleted = false;

    /**
     * Constructs a new Customer with the specified details.
     *
     * @param id       unique identifier for the customer
     * @param name     customer's full name
     * @param phone    customer's phone number
     * @param email    customer's email address
     * @param password customer's login password
     */
    public Customer(int id, String name, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.bookings = new ArrayList<>();
    }

    /**
     * @return the customer's unique identifier
     */
    public int getId() {
        return id;
    }

    /**
     * @return the customer's full name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the customer's phone number
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @return the customer's email address
     */
    public String getEmail() {
        return email;
    }

    /**
     * @return the customer's login password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @return true if the customer has been soft-deleted, false otherwise
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * @return a defensive copy of the customer's booking list
     */
    public List<Booking> getBookings() {
        return new ArrayList<>(bookings);
    }

    /**
     * Updates the customer's name.
     * 
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Updates the customer's phone number.
     * 
     * @param phone the new phone number
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Updates the customer's email address.
     * 
     * @param email the new email address
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Updates the customer's login password.
     * 
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Updates the customer's deleted status.
     * 
     * @param deleted true to mark as deleted, false otherwise
     */
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    /**
     * Adds a new booking to the customer's booking list.
     * 
     * @param booking the booking to add
     */
    public void addBooking(Booking booking) {
        bookings.add(booking);
    }

    /**
     * Removes a booking from the customer's booking list.
     * 
     * @param booking the booking to remove
     */
    public void cancelBooking(Booking booking) {
        bookings.remove(booking);
    }

    /**
     * Returns a short string representation of the customer's details.
     *
     * @return a string containing customer ID, name, phone, and email
     */
    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + " - " + email;
    }
}