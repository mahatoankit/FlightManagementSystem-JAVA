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
    private JTextField flightNoField = new JTextField();
    private JTextField originField = new JTextField();
    private JTextField destinationField = new JTextField();
    private JTextField departureDateField = new JTextField();
    private JTextField priceField = new JTextField();
    private JTextField capacityField = new JTextField();
    private JButton addBtn = new JButton("Add");

    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add New Flight");
        setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridLayout(7, 2, 5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        mainPanel.add(new JLabel("Flight Number:"));
        mainPanel.add(flightNoField);
        mainPanel.add(new JLabel("Origin:"));
        mainPanel.add(originField);
        mainPanel.add(new JLabel("Destination:"));
        mainPanel.add(destinationField);
        mainPanel.add(new JLabel("Departure Date (YYYY-MM-DD):"));
        mainPanel.add(departureDateField);
        mainPanel.add(new JLabel("Price:"));
        mainPanel.add(priceField);
        mainPanel.add(new JLabel("Capacity:"));
        mainPanel.add(capacityField);
        mainPanel.add(new JLabel(""));
        mainPanel.add(addBtn);

        addBtn.addActionListener(this);

        setContentPane(mainPanel);
        setLocationRelativeTo(mw);
        setVisible(true);
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