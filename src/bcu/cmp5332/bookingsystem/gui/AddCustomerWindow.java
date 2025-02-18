package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCustomerWindow extends JFrame implements ActionListener {

    private final MainWindow mw;
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private final JButton addBtn = new JButton("Add");
    private final JButton cancelBtn = new JButton("Cancel");

    public AddCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add New Customer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(mw);

        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Name field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(nameField, gbc);

        // Phone field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(phoneField, gbc);

        // Email field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        formPanel.add(emailField, gbc);

        // Style buttons
        addBtn.setBackground(new Color(46, 125, 50));
        addBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(new Color(198, 40, 40));
        cancelBtn.setForeground(Color.WHITE);

        // Add action listeners
        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        // Add buttons to panel
        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        // Combine panels
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == addBtn) {
            addCustomer();
        } else if (ev.getSource() == cancelBtn) {
            dispose();
        }
    }

    private void addCustomer() {
        try {
            // Input validation
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                throw new FlightBookingSystemException("All fields are required.");
            }

            // Email validation
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new FlightBookingSystemException("Invalid email format.");
            }

            // Phone validation
            if (!phone.matches("^[0-9+()-]{10,15}$")) {
                throw new FlightBookingSystemException("Invalid phone number format.");
            }

            // Get the flight booking system and add the customer
            FlightBookingSystem fbs = mw.getFlightBookingSystem();
            fbs.addCustomer(name, phone, email);

            // After successful addition, show the generated password
            Customer lastAdded = fbs.getCustomers().stream()
                    .filter(c -> c.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new FlightBookingSystemException("Failed to retrieve added customer"));

            // Show success message with password
            JOptionPane.showMessageDialog(this,
                    "Customer added successfully!\nTemporary password: " + lastAdded.getPassword() +
                            "\nPlease make sure to save this password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Save the updated system state
            FlightBookingSystemData.store(fbs);

            // Refresh the customers display in main window
            mw.displayAllCustomers();

            // Close the add customer window
            dispose();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}