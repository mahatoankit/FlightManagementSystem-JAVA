package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateCustomerWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField customerIdField = new JTextField();
    private JTextField nameField = new JTextField();
    private JTextField phoneField = new JTextField();
    private JTextField emailField = new JTextField();
    private JButton loadBtn = new JButton("Load");
    private JButton updateBtn = new JButton("Update");

    public UpdateCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Update Customer");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Create main panel
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add components
        mainPanel.add(new JLabel("Customer ID:"));
        JPanel idPanel = new JPanel(new BorderLayout(5, 0));
        idPanel.add(customerIdField, BorderLayout.CENTER);
        idPanel.add(loadBtn, BorderLayout.EAST);
        mainPanel.add(idPanel);

        mainPanel.add(new JLabel("Name:"));
        mainPanel.add(nameField);
        mainPanel.add(new JLabel("Phone:"));
        mainPanel.add(phoneField);
        mainPanel.add(new JLabel("Email:"));
        mainPanel.add(emailField);
        mainPanel.add(new JLabel(""));
        mainPanel.add(updateBtn);

        // Disable fields initially
        nameField.setEnabled(false);
        phoneField.setEnabled(false);
        emailField.setEnabled(false);
        updateBtn.setEnabled(false);

        // Add action listeners
        loadBtn.addActionListener(this);
        updateBtn.addActionListener(this);

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadBtn) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                Customer customer = mw.getFlightBookingSystem().getCustomerByID(customerId);

                // Populate fields with customer data
                nameField.setText(customer.getName());
                phoneField.setText(customer.getPhone());
                emailField.setText(customer.getEmail());

                // Enable fields for editing
                nameField.setEnabled(true);
                phoneField.setEnabled(true);
                emailField.setEnabled(true);
                updateBtn.setEnabled(true);

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Please enter a valid customer ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == updateBtn) {
            try {
                int customerId = Integer.parseInt(customerIdField.getText().trim());
                String name = nameField.getText().trim();
                String phone = phoneField.getText().trim();
                String email = emailField.getText().trim();

                // Validate inputs
                if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                    throw new FlightBookingSystemException("All fields must be filled");
                }

                // Update customer
                mw.getFlightBookingSystem().updateCustomer(customerId, name, phone, email);

                // Show success message and refresh display
                JOptionPane.showMessageDialog(this,
                        "Customer updated successfully", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                mw.displayAllCustomers();
                dispose();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                        "Invalid customer ID", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (FlightBookingSystemException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}