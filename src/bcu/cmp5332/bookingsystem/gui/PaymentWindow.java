package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

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
    
    private final MainWindow parentWindow;
    private final Booking booking;

    /**
     * Constructs a new PaymentWindow with dark theme styling.
     * @param parent The parent MainWindow instance
     * @param booking The Booking instance for payment processing
     */
    public PaymentWindow(MainWindow parent, Booking booking) {
        super(parent, "Process Payment", true);
        this.parentWindow = parent;
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

        // Styled payment method selector
        gbc.gridy = 2;
        String[] paymentMethods = { "Credit Card", "Debit Card", "Bank Transfer" };
        JComboBox<String> methodCombo = createStyledComboBox(paymentMethods);
        mainPanel.add(methodCombo, gbc);

        // Styled process button
        gbc.gridy = 3;
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
            booking.setPaymentProcessed(true);
            showSuccessMessage("Payment processed successfully!");
            parentWindow.refreshBookingsDisplay();
            dispose();
        } catch (Exception ex) {
            showErrorMessage("Error processing payment: " + ex.getMessage());
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
}