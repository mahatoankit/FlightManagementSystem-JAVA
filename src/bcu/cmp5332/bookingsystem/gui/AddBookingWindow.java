package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class AddBookingWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField custIdField = new JTextField(10);
    private JTextField flightIdField = new JTextField(10);
    private JLabel feeLabel = new JLabel("Booking Fee: $0.00");
    private JButton computeFeeButton = new JButton("Compute Fee");
    private JButton bookButton = new JButton("Book Flight");

    private double computedFee = 0.0;

    public AddBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Create New Booking");
        setSize(400, 300);
        setLayout(new BorderLayout(10, 10));

        // Main panel with modern grid layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Style components
        custIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        flightIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Style buttons
        computeFeeButton.setBackground(new Color(70, 130, 180));
        computeFeeButton.setForeground(Color.WHITE);
        computeFeeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        computeFeeButton.setFocusPainted(false);

        bookButton.setBackground(new Color(46, 139, 87));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        bookButton.setFocusPainted(false);

        // Add components with proper spacing
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(new JLabel("Customer ID:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(custIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Flight ID:"), gbc);
        gbc.gridx = 1;
        mainPanel.add(flightIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        mainPanel.add(computeFeeButton, gbc);

        gbc.gridy = 3;
        mainPanel.add(feeLabel, gbc);

        gbc.gridy = 4;
        mainPanel.add(bookButton, gbc);

        // If a customer is logged in, preselect their ID and disable editing
        if (mw.getLoggedInCustomerId() != null) {
            custIdField.setText(String.valueOf(mw.getLoggedInCustomerId()));
            custIdField.setEditable(false);
        }

        computeFeeButton.addActionListener(this);
        bookButton.addActionListener(this);

        // Add hover effects
        addButtonHoverEffect(computeFeeButton);
        addButtonHoverEffect(bookButton);

        add(mainPanel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setResizable(false);
        setVisible(true);
    }

    private void addButtonHoverEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(button.getBackground().darker());
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computeFeeButton) {
            computeFee();
        } else if (e.getSource() == bookButton) {
            createBooking();
        }
    }

    private void computeFee() {
        try {
            int flightId = Integer.parseInt(flightIdField.getText());
            double fee = mw.getFlightBookingSystem().getFlightByID(flightId).calculatePrice(LocalDate.now());
            computedFee = fee;
            feeLabel.setText(String.format("Booking Fee: $%.2f", fee));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Flight ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createBooking() {
        try {
            int custId = Integer.parseInt(custIdField.getText());
            int flightId = Integer.parseInt(flightIdField.getText());
            AddBooking addCmd = new AddBooking(custId, flightId, LocalDate.now());
            addCmd.execute(mw.getFlightBookingSystem());
            Booking latestBooking = mw.getFlightBookingSystem().getBookings()
                    .get(mw.getFlightBookingSystem().getBookings().size() - 1);
            JOptionPane.showMessageDialog(this,
                    "Booking created successfully.\nFee: $" + String.format("%.2f", computedFee));
            mw.displayBookings();
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
