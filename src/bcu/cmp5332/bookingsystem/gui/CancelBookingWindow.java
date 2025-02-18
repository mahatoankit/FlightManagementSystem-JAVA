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
    private final Integer customerId;

    // Add color constants
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);
    private static final Color ERROR_COLOR = new Color(255, 87, 34);

    public CancelBookingWindow(MainWindow mw) {
        this.mw = mw;
        this.customerId = null;
        initialize();
    }

    public CancelBookingWindow(MainWindow mw, Integer customerId) {
        this.mw = mw;
        this.customerId = customerId;
        initialize();
    }

    private void initialize() {
        setTitle("Cancel Booking");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 200);

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(DARKER_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components
        JLabel titleLabel = new JLabel("Cancel Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(titleLabel, gbc);

        // Booking ID field
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        JLabel idLabel = createStyledLabel("Booking ID:");
        inputPanel.add(idLabel, gbc);

        gbc.gridx = 1;
        styleTextField(bookingIdField);
        inputPanel.add(bookingIdField, gbc);

        // Cancel button
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        cancelBtn = createStyledButton("Cancel Booking");
        cancelBtn.addActionListener(this);
        inputPanel.add(cancelBtn, gbc);

        mainPanel.add(inputPanel, BorderLayout.CENTER);
        add(mainPanel);

        setLocationRelativeTo(mw);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(ACCENT_COLOR.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(ACCENT_COLOR);
            }
        });

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelBtn) {
            try {
                int bookingId = Integer.parseInt(bookingIdField.getText().trim());
                Booking booking = mw.getFlightBookingSystem().getBookingByID(bookingId);

                if (customerId != null && booking.getCustomer().getId() != customerId) {
                    throw new FlightBookingSystemException("You can only cancel your own bookings.");
                }

                double cancellationFee = 0.15 * booking.getBookingFee();
                int confirm = JOptionPane.showConfirmDialog(this,
                        "A cancellation fee of $" + String.format("%.2f", cancellationFee)
                                + " will be applied. Confirm cancellation?",
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
}