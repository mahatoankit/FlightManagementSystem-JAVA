package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Provides a window interface for updating existing flight bookings in the system.
 * This class extends JFrame and implements ActionListener to handle user interactions.
 * 
 * @author Flight Booking System Team
 * @version 1.0
 */
public class UpdateBookingWindow extends JFrame implements ActionListener {

    /** Reference to the main window */
    private MainWindow mw;
    /** Text field for booking ID input */
    private JTextField bookingIdField = new JTextField(10);
    /** Text field for new flight ID input */
    private JTextField newFlightIdField = new JTextField(10);
    /** Button to trigger booking update */
    private JButton updateBtn = new JButton("Update Booking");

    /**
     * Constructs a new UpdateBookingWindow with a reference to the main window.
     * 
     * @param mw The MainWindow instance that created this window
     */
    public UpdateBookingWindow(MainWindow mw) {
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

        setTitle("Update Booking");
        setSize(350, 150);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        panel.add(createStyledLabel("Booking ID:"));
        panel.add(bookingIdField);
        panel.add(createStyledLabel("New Flight ID:"));
        panel.add(newFlightIdField);
        panel.add(new JLabel(""));
        panel.add(createStyledButton(updateBtn));

        updateBtn.addActionListener(this);

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
     * Validates input and executes the update booking command.
     * Shows success message on successful update and error messages for any failures.
     * 
     * @param e The ActionEvent triggered by the user
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int bookingId = Integer.parseInt(bookingIdField.getText());
            int newFlightId = Integer.parseInt(newFlightIdField.getText());
            UpdateBooking updateCmd = new UpdateBooking(bookingId, newFlightId);
            updateCmd.execute(mw.getFlightBookingSystem());
            JOptionPane.showMessageDialog(this, "Booking updated successfully.");
            mw.displayBookings();
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}