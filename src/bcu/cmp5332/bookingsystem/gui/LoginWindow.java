package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;
    private JTextField userIdField;
    private JPasswordField passwordField;

    public LoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Flight Booking System - Login");
        setSize(400, 200);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // User ID field
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("User ID:"), gbc);

        gbc.gridx = 1;
        userIdField = new JTextField(15);
        mainPanel.add(userIdField, gbc);

        // Password field
        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel();
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(this);
        JButton adminButton = new JButton("Admin Login");
        adminButton.addActionListener(e -> adminLogin());

        buttonsPanel.add(loginButton);
        buttonsPanel.add(adminButton);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(buttonsPanel, gbc);

        add(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            String password = new String(passwordField.getPassword());

            // Here you would implement actual authentication logic
            // For now, let's just check if the user exists
            try {
                Customer customer = fbs.getCustomerByID(userId);
                // In a real system, you would verify the password here
                MainWindow mainWindow = new MainWindow(fbs);
                mainWindow.setAdminMode(false);
                mainWindow.setLoggedInCustomerId(userId);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid credentials",
                        "Login Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid user ID",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void adminLogin() {
        String password = JOptionPane.showInputDialog(this,
                "Enter admin password:",
                "Admin Login",
                JOptionPane.PLAIN_MESSAGE);

        if (password != null && password.equals("admin")) { // In real system, use proper authentication
            MainWindow mainWindow = new MainWindow(fbs);
            mainWindow.setAdminMode(true);
            dispose();
        } else if (password != null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid admin password",
                    "Login Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}