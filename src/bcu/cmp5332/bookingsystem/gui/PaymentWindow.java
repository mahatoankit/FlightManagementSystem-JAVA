package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;

/**
 * Provides a dialog window for processing payments for flight bookings.
 * This class extends JDialog to create a modal payment processing interface.
 * 
 * @author Flight Booking System Team
 * @version 1.0
 */
public class PaymentWindow extends JDialog {
    
    /** Serial version UID for serialization */
    private static final long serialVersionUID = 1L;
    /** Reference to the parent main window */
    private final MainWindow parentWindow;
    /** The booking for which payment is being processed */
    private final Booking booking;

    /**
     * Constructs a new PaymentWindow for processing a booking payment.
     * 
     * @param parent The parent MainWindow instance
     * @param booking The Booking instance for which payment is to be processed
     */
    public PaymentWindow(MainWindow parent, Booking booking) {
        super(parent, "Process Payment", true);
        this.parentWindow = parent;
        this.booking = booking;
        initialize();
    }

    /**
     * Initializes the payment window components and sets up the GUI layout.
     * Creates and positions all payment-related UI elements including
     * booking details, payment method selection, and process button.
     */
    private void initialize() {
        setSize(400, 300);
        setLocationRelativeTo(getParent());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        // Payment details
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Booking ID: " + booking.getId()), gbc);

        gbc.gridy = 1;
        mainPanel.add(new JLabel("Amount to Pay: $" + booking.getBookingFee()), gbc);

        // Payment method selection
        gbc.gridy = 2;
        String[] paymentMethods = { "Credit Card", "Debit Card", "Bank Transfer" };
        JComboBox<String> methodCombo = new JComboBox<>(paymentMethods);
        mainPanel.add(methodCombo, gbc);

        // Process button
        gbc.gridy = 3;
        JButton processButton = new JButton("Process Payment");
        processButton.addActionListener(e -> processPayment());
        mainPanel.add(processButton, gbc);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Processes the payment for the booking.
     * Sets the booking's payment status to processed and shows appropriate
     * success or error messages. Updates the parent window's booking display
     * after successful payment processing.
     */
    private void processPayment() {
        try {
            booking.setPaymentProcessed(true);
            JOptionPane.showMessageDialog(this,
                    "Payment processed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            parentWindow.refreshBookingsDisplay();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error processing payment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}