package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField flightNoField = new JTextField(50); // Increased from default
    private JTextField originField = new JTextField(20); // Increased from default
    private JTextField destinationField = new JTextField(20); // Increased from default
    private JTextField departureDateField = new JTextField(20);// Increased from default
    private JTextField priceField = new JTextField(20); // Increased from default
    private JTextField capacityField = new JTextField(20); // Increased from default
    private JButton addBtn = new JButton("Add");

    // Add color constants
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add New Flight");
        setSize(700, 600); // Increased width from 600 to 700

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Title label
        JLabel titleLabel = new JLabel("Add New Flight");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24)); // Increased font size
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 1.0; // Added weight to make it expand horizontally
        gbc.fill = GridBagConstraints.HORIZONTAL; // Make it fill horizontally
        mainPanel.add(titleLabel, gbc);

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(DARKER_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        // Add form components
        addFormField(formPanel, "Flight Number:", flightNoField, 1);
        addFormField(formPanel, "Origin:", originField, 2);
        addFormField(formPanel, "Destination:", destinationField, 3);
        addFormField(formPanel, "Departure Date (YYYY-MM-DD):", departureDateField, 4);
        addFormField(formPanel, "Price:", priceField, 5);
        addFormField(formPanel, "Capacity:", capacityField, 6);

        // Style and add the button
        addBtn = createStyledButton("Add Flight");
        addBtn.addActionListener(this);
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(15, 5, 5, 5);
        formPanel.add(addBtn, gbc);

        // Add form panel to main panel
        gbc.gridy = 1;
        gbc.insets = new Insets(10, 0, 0, 0);
        mainPanel.add(formPanel, gbc);

        setContentPane(mainPanel);
        setLocationRelativeTo(mw);
        setVisible(true);
        }

        private void addFormField(JPanel panel, String labelText, JTextField field, int row) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.2; // Give less weight to label
        panel.add(label, gbc);

        // Text field
        styleTextField(field);
        gbc.gridx = 1;
        gbc.weightx = 0.8; // Give more weight to field
        panel.add(field, gbc);
        }

        private void styleTextField(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(ACCENT_COLOR);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

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
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            try {
                // Debug prints
                System.out.println("Attempting to add flight with following details:");
                System.out.println("Flight Number: " + flightNoField.getText());
                System.out.println("Origin: " + originField.getText());
                System.out.println("Destination: " + destinationField.getText());
                System.out.println("Date: " + departureDateField.getText());
                System.out.println("Price: " + priceField.getText());
                System.out.println("Capacity: " + capacityField.getText());

                // Validate date format
                LocalDate departureDate;
                try {
                    departureDate = LocalDate.parse(departureDateField.getText(),
                            DateTimeFormatter.ISO_LOCAL_DATE);
                } catch (DateTimeParseException e) {
                    throw new FlightBookingSystemException(
                            "Invalid date format. Please use YYYY-MM-DD");
                }

                // Validate price
                double price;
                try {
                    price = Double.parseDouble(priceField.getText());
                    if (price <= 0) {
                        throw new FlightBookingSystemException("Price must be greater than 0");
                    }
                } catch (NumberFormatException e) {
                    throw new FlightBookingSystemException("Invalid price format");
                }

                // Validate capacity
                int capacity;
                try {
                    capacity = Integer.parseInt(capacityField.getText());
                    if (capacity <= 0) {
                        throw new FlightBookingSystemException("Capacity must be greater than 0");
                    }
                } catch (NumberFormatException e) {
                    throw new FlightBookingSystemException("Invalid capacity format");
                }

                // Create and execute command
                AddFlight addFlight = new AddFlight(
                        flightNoField.getText(),
                        originField.getText(),
                        destinationField.getText(),
                        departureDate,
                        price,
                        capacity);
                addFlight.execute(mw.getFlightBookingSystem());

                // Debug print
                System.out.println("Flight added successfully");

                // Refresh the flights display and close the window
                mw.displayUpcomingFlights();
                this.dispose();
                JOptionPane.showMessageDialog(null,
                        "Flight added successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (FlightBookingSystemException ex) {
                // Debug print
                System.out.println("Error adding flight: " + ex.getMessage());
                JOptionPane.showMessageDialog(this, ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}