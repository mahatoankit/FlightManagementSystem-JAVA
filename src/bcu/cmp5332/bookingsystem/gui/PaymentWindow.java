package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Payment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.time.LocalDate;

/**
 * Provides a dialog window for processing payments for flight bookings.
 * Features a modern dark theme and minimalist design.
 */
public class PaymentWindow extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);

    private final MainWindow mw;
    private final Booking booking;
    private JTextField cardNumberField;
    private JTextField expiryDateField;

    /**
     * Constructs a new PaymentWindow with dark theme styling.
     * 
     * @param parent  The parent MainWindow instance
     * @param booking The Booking instance for payment processing
     */
    public PaymentWindow(MainWindow parent, Booking booking) {
        super(parent, "Process Payment", true);
        this.mw = parent; // Initialize mw field
        this.booking = booking;
        initialize();
    }

    /**
     * Initializes the window components with dark theme styling.
     */
    private void initialize() {
        setSize(450, 350);
        setLocationRelativeTo(getParent());
        getContentPane().setBackground(DARK_BG);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Booking details with styled labels
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(createStyledLabel("Booking ID: " + booking.getId()), gbc);

        gbc.gridy = 1;
        mainPanel.add(createStyledLabel("Amount to Pay: $" + booking.getBookingFee()), gbc);

        // Card number field
        gbc.gridy = 2;
        mainPanel.add(createStyledLabel("Card Number:"), gbc);
        gbc.gridy = 3;
        cardNumberField = new JTextField(20);
        styleTextField(cardNumberField);
        mainPanel.add(cardNumberField, gbc);

        // Expiry date field
        gbc.gridy = 4;
        mainPanel.add(createStyledLabel("Expiry Date (MM/YY):"), gbc);
        gbc.gridy = 5;
        expiryDateField = new JTextField(20);
        styleTextField(expiryDateField);
        mainPanel.add(expiryDateField, gbc);

        // Process button
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton processButton = createStyledButton("Process Payment");
        processButton.addActionListener(e -> processPayment());
        mainPanel.add(processButton, gbc);

        add(mainPanel);
        setVisible(true);
    }

    /**
     * Creates a styled label with dark theme colors.
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Dialog", Font.PLAIN, 14));
        return label;
    }

    /**
     * Creates a styled combo box with dark theme colors.
     */
    private JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(DARKER_BG);
        comboBox.setForeground(TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR));
        ((JComponent) comboBox.getRenderer()).setBackground(DARKER_BG);
        return comboBox;
    }

    /**
     * Creates a styled button with dark theme colors.
     */
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Dialog", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    /**
     * Processes the payment with styled success/error messages.
     */
    private void processPayment() {
        try {
            // Get payment details
            String cardNumber = cardNumberField.getText();
            String expiryDate = expiryDateField.getText();

            // Validate payment details
            if (!validatePaymentDetails(cardNumber, expiryDate)) {
                return;
            }

            // Create payment object
            Payment payment = new Payment(
                    booking.getId(),
                    booking.getBookingFee(),
                    cardNumber,
                    expiryDate,
                    LocalDate.now());

            // Add payment to the system
            mw.getFlightBookingSystem().addPayment(payment);
            booking.setPaymentProcessed(true);

            // Store the updated data
            FlightBookingSystemData.store(mw.getFlightBookingSystem());

            // Show success message
            JOptionPane.showMessageDialog(this,
                    "Payment processed successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Refresh the bookings display
            mw.refreshBookingsDisplay();

            // Close the payment window
            dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Error processing payment: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Shows a styled success message dialog.
     */
    private void showSuccessMessage(String message) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", SUCCESS_COLOR);
        JOptionPane.showMessageDialog(this, message, "Success",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Shows a styled error message dialog.
     */
    private void showErrorMessage(String message) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", Color.RED);
        JOptionPane.showMessageDialog(this, message, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Styles a text field with dark theme colors.
     */
    private void styleTextField(JTextField field) {
        field.setBackground(DARKER_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    /**
     * Validates the payment details.
     */
    private boolean validatePaymentDetails(String cardNumber, String expiryDate) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            showErrorMessage("Please enter a card number");
            return false;
        }
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            showErrorMessage("Please enter an expiry date");
            return false;
        }
        if (!expiryDate.matches("\\d{2}-\\d{2}")) {
            showErrorMessage("Expiry date must be in MM-YY format");
            return false;
        }
        return true;
    }
}