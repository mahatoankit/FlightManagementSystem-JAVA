package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Customer;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A window for updating customer details in the Flight Booking System.
 * Features a modern dark theme and minimalist design.
 */
public class UpdateCustomerWindow extends JFrame implements ActionListener {

    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    
    private MainWindow mw;
    private JTextField customerIdField;
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JButton loadBtn;
    private JButton updateBtn;

    /**
     * Constructs a new UpdateCustomerWindow.
     * @param mw The main window instance
     */
    public UpdateCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initializes the window components and sets up the UI.
     */
    private void initialize() {
        setTitle("Update Customer");
        setSize(450, 350);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(DARK_BG);

        // Initialize components with custom styling
        customerIdField = createStyledTextField();
        nameField = createStyledTextField();
        phoneField = createStyledTextField();
        emailField = createStyledTextField();
        
        loadBtn = createStyledButton("Load");
        updateBtn = createStyledButton("Update");

        // Create main panel
        JPanel mainPanel = new JPanel(new GridLayout(5, 2, 15, 15));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components
        mainPanel.add(createStyledLabel("Customer ID:"));
        JPanel idPanel = new JPanel(new BorderLayout(10, 0));
        idPanel.setBackground(DARK_BG);
        idPanel.add(customerIdField, BorderLayout.CENTER);
        idPanel.add(loadBtn, BorderLayout.EAST);
        mainPanel.add(idPanel);

        mainPanel.add(createStyledLabel("Name:"));
        mainPanel.add(nameField);
        mainPanel.add(createStyledLabel("Phone:"));
        mainPanel.add(phoneField);
        mainPanel.add(createStyledLabel("Email:"));
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

    /**
     * Creates a styled JTextField with dark theme.
     * @return styled JTextField
     */
    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setBackground(DARKER_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        return field;
    }

    /**
     * Creates a styled JButton with dark theme.
     * @param text Button text
     * @return styled JButton
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Creates a styled JLabel with dark theme.
     * @param text Label text
     * @return styled JLabel
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Dialog", Font.PLAIN, 12));
        return label;
    }

    /**
     * Handles button click events.
     * @param e The action event
     */
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
                showErrorMessage("Please enter a valid customer ID");
            } catch (FlightBookingSystemException ex) {
                showErrorMessage(ex.getMessage());
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
                        "Customer updated successfully",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                mw.displayAllCustomers();
                dispose();

            } catch (NumberFormatException ex) {
                showErrorMessage("Invalid customer ID");
            } catch (FlightBookingSystemException ex) {
                showErrorMessage(ex.getMessage());
            }
        }
    }

    /**
     * Shows an error message dialog.
     * @param message The error message to display
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}