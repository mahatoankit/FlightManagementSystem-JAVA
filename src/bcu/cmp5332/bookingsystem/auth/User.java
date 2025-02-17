package bcu.cmp5332.bookingsystem.auth;

public class User {
    private String username;
    private String password; // For simplicity, we store plaintext (in a real system use hashing)
    private Role role;
    
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
    
    public String getUsername() {
        return username;
    }
    
    public Role getRole() {
        return role;
    }
    
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }
}
