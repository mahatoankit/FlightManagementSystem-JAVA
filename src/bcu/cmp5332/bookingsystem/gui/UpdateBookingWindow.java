package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.UpdateBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class UpdateBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField bookingIdField = new JTextField(10);
    private JTextField newFlightIdField = new JTextField(10);
    private JButton updateBtn = new JButton("Update Booking");

    public UpdateBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

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