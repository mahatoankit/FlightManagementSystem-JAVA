package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;

public class PaymentWindow extends JDialog {
    private static final long serialVersionUID = 1L;
    private final MainWindow parentWindow;
    private final Booking booking;

    public PaymentWindow(MainWindow parent, Booking booking) {
        super(parent, "Process Payment", true);
        this.parentWindow = parent;
        this.booking = booking;
        initialize();
    }

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
