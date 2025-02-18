package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a window interface for updating existing flight bookings in the system.
 * Features a modern dark theme and minimalist design.
 */
public class UpdateBookingWindow extends JFrame implements ActionListener {

    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);

    private MainWindow mw;
    private JTextField bookingIdField;
    private JTextField newFlightIdField;
    private JButton updateBtn;

    /**
     * Constructs a new UpdateBookingWindow with a reference to the main window.
     * @param mw The MainWindow instance that created this window
     */
    public UpdateBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initializes the window components and sets up the GUI layout.
     */
    private void initialize() {
        setTitle("Update Booking");
        setSize(400, 200);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(DARK_BG);

        // Initialize components
        bookingIdField = createStyledTextField();
        newFlightIdField = createStyledTextField();
        updateBtn = createStyledButton("Update Booking");

        // Create main panel
        JPanel mainPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add components
        mainPanel.add(createStyledLabel("Booking ID:"));
        mainPanel.add(bookingIdField);
        mainPanel.add(createStyledLabel("New Flight ID:"));
        mainPanel.add(newFlightIdField);
        mainPanel.add(new JLabel(""));
        mainPanel.add(updateBtn);

        // Add action listener
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
     * Shows an error message dialog with dark theme styling.
     * @param message The error message to display
     */
    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Handles the action events for the update button.
     * @param e The ActionEvent triggered by the user
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText().trim());
            int newFlightId = Integer.parseInt(newFlightIdField.getText().trim());
            UpdateBooking updateCmd = new UpdateBooking(bookingId, newFlightId);
            updateCmd.execute(mw.getFlightBookingSystem());
            
            JOptionPane.showMessageDialog(this,
                    "Booking updated successfully",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            mw.displayBookings();
            dispose();
        } catch (NumberFormatException ex) {
            showErrorMessage("Invalid input. Please check IDs.");
        } catch (FlightBookingSystemException ex) {
            showErrorMessage(ex.getMessage());
        }
    }
}