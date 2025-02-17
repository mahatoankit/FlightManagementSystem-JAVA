package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a window interface for updating existing customer details in the system.
 * This class extends JFrame and implements ActionListener to handle user interactions.
 * 
 * @author Flight Booking System Team
 * @version 1.0
 */
public class UpdateCustomerWindow extends JFrame implements ActionListener {

    /** Reference to the main window */
    private MainWindow mw;
    /** Text field for customer ID input */
    private JTextField custIdField = new JTextField(10);
    /** Text field for customer name input */
    private JTextField nameField = new JTextField(20);
    /** Text field for customer phone input */
    private JTextField phoneField = new JTextField(15);
    /** Text field for customer email input */
    private JTextField emailField = new JTextField(30);
    /** Password field for customer password input */
    private JPasswordField passwordField = new JPasswordField(20);
    /** Button to trigger customer update */
    private JButton updateButton = new JButton("Update Customer");

    /**
     * Constructs a new UpdateCustomerWindow with a reference to the main window.
     * 
     * @param mw The MainWindow instance that created this window
     */
    public UpdateCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initializes the window components and sets up the GUI layout.
     * Configures the look and feel, creates and positions all UI elements.
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Update Customer");
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        panel.add(createStyledLabel("Customer ID:"));
        panel.add(custIdField);
        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Phone:"));
        panel.add(phoneField);
        panel.add(createStyledLabel("Email:"));
        panel.add(emailField);
        panel.add(createStyledLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(createStyledButton(updateButton));

        updateButton.addActionListener(this);

        getContentPane().add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    /**
     * Creates a styled JLabel with consistent formatting.
     * 
     * @param text The text to display in the label
     * @return A styled JLabel instance
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    /**
     * Creates a styled JButton with consistent formatting and appearance.
     * 
     * @param button The JButton to style
     * @return A styled JButton instance
     */
    private JButton createStyledButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * Handles the action events for the update button.
     * Validates input and executes the update customer command.
     * Shows success message on successful update and error messages for any failures.
     * 
     * @param e The ActionEvent triggered by the user
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int custId = Integer.parseInt(custIdField.getText());
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            UpdateCustomer updateCmd = new UpdateCustomer(custId, name, phone, email, password);
            updateCmd.execute(mw.getFlightBookingSystem());
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            mw.displayAllCustomers();
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Customer ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}