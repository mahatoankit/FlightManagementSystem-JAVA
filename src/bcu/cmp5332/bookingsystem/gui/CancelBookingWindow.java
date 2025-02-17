package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * A GUI window that allows users to cancel existing flight bookings.
 * This class provides functionality to cancel a booking by its ID and
 * handles the cancellation fee calculation and confirmation process.
 */
public class CancelBookingWindow extends JFrame implements ActionListener {

    /** Serialization ID for the class */
    private static final long serialVersionUID = 1L;
    
    /** Reference to the main window */
    private MainWindow mw;
    
    /** Text field for entering the booking ID */
    private JTextField bookingIdField = new JTextField(10);
    
    /** Button to initiate the booking cancellation */
    private JButton cancelBtn = new JButton("Cancel Booking");

    /**
     * Constructs a new CancelBookingWindow.
     * 
     * @param mw The MainWindow instance that created this window
     */
    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

/**
 * Initializes the Cancel Booking window components and sets up the GUI layout.
 * Configures the look and feel, creates input fields, and adds the cancel button.
 * Sets the window title, size, and layout, and makes the window visible.
 */

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Cancel Booking");
        setSize(350, 150);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        panel.add(createStyledLabel("Booking ID:"));
        panel.add(bookingIdField);
        panel.add(new JLabel(""));
        panel.add(createStyledButton(cancelBtn));

        cancelBtn.addActionListener(this);

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
 * Handles the action event for the cancel button.
 * Retrieves the booking ID from the input field, calculates the cancellation fee,
 * and prompts the user for confirmation to proceed with cancellation.
 * If the user confirms, executes the cancellation command and updates the booking display.
 * Catches and displays error messages for invalid input or system exceptions.
 *
 * @param e The ActionEvent triggered by the cancel button
 */

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            Booking booking = mw.getFlightBookingSystem().getBookingByID(bookingId);
            double cancellationFee = 0.15 * booking.getBookingFee();
            int confirm = JOptionPane.showConfirmDialog(this,
                "A cancellation fee of $" + String.format("%.2f", cancellationFee) + " will be applied. Confirm cancellation?",
                "Confirm Cancellation", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                CancelBooking cancelCmd = new CancelBooking(bookingId, cancellationFee);
                cancelCmd.execute(mw.getFlightBookingSystem());
                mw.displayBookings();
                this.dispose();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input for booking ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}