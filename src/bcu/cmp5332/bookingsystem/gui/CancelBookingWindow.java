package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.CancelBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CancelBookingWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private MainWindow mw;
    private JTextField bookingIdField = new JTextField(10);
    private JButton cancelBtn = new JButton("Cancel Booking");

    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

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

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JButton createStyledButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

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