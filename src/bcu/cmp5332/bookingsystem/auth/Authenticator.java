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
    
    public User login(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username) && user.checkPassword(password)) {
                return user;
            }
        }
        return null;
    }
}
