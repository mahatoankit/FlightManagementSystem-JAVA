package bcu.cmp5332.bookingsystem.auth;

import java.util.ArrayList;
import java.util.List;

public class Authenticator {
    private List<User> users = new ArrayList<>();
    
    public Authenticator() {
        // Pre-populate with demo users
        users.add(new User("admin", "admin123", Role.ADMIN));
        users.add(new User("customer", "cust123", Role.CUSTOMER));
    }
    
    /**
     * Authenticates a user by checking the given username and password against
     * the registered users. If the credentials are valid, returns the User
     * object associated with the username. Otherwise, returns null.
     *
     * @param username The username to check
     * @param password The password to check
     * @return The associated User object, or null if the credentials are invalid
     */
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }
}
