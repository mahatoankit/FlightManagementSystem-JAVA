package bcu.cmp5332.bookingsystem.model;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private int id;
    private String name;
    private String phone;
    private String email;
    private String password; // New field for password
    private final List<Booking> bookings;
    private boolean isDeleted = false;

    public Customer(int id, String name, String phone, String email, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.bookings = new ArrayList<>();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public boolean isDeleted() { return isDeleted; }
    public List<Booking> getBookings() { return new ArrayList<>(bookings); }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setDeleted(boolean deleted) { this.isDeleted = deleted; }

    public void addBooking(Booking booking) { bookings.add(booking); }
    public void cancelBooking(Booking booking) { bookings.remove(booking); }

    public String getDetailsShort() {
        return "Customer #" + id + " - " + name + " - " + phone + " - " + email;
    }
}
