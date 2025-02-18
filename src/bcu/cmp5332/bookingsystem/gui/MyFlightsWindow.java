package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;

public class MyFlightsWindow extends JFrame {
    private final FlightBookingSystem fbs;
    private final int customerId;
    private JTable flightsTable;

    public MyFlightsWindow(FlightBookingSystem fbs, int customerId) {
        this.fbs = fbs;
        this.customerId = customerId;
        initialize();
    }

    private void initialize() {
        setTitle("My Flights");
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));

        // Create table model
        String[] columns = { "Flight Number", "From", "To", "Date", "Status", "Price", "Payment Status" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Booking> bookings = customer.getBookings();

            for (Booking booking : bookings) {
                Flight flight = booking.getFlight();
                String status = flight.getDepartureDate().isAfter(LocalDate.now()) ? "Upcoming" : "Past";
                String paymentStatus = booking.isPaymentProcessed() ? "Paid" : "Pending";

                model.addRow(new Object[] {
                        flight.getFlightNumber(),
                        flight.getOrigin(),
                        flight.getDestination(),
                        flight.getDepartureDate(),
                        status,
                        String.format("$%.2f", booking.getBookingFee()),
                        paymentStatus
                });
            }
        } catch (FlightBookingSystemException e) {
            JOptionPane.showMessageDialog(this,
                    "Error loading flights: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Create and style the table
        flightsTable = new JTable(model);
        flightsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        flightsTable.setRowHeight(30);
        flightsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        flightsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(flightsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create summary panel
        JPanel summaryPanel = createSummaryPanel();

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 10, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(245, 245, 245));

        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Booking> bookings = customer.getBookings();

            int upcomingFlights = 0;
            double totalSpent = 0.0;

            for (Booking booking : bookings) {
                if (booking.getFlight().getDepartureDate().isAfter(LocalDate.now())) {
                    upcomingFlights++;
                }
                totalSpent += booking.getBookingFee();
            }

            JLabel totalLabel = new JLabel(String.format("Total Flights: %d", bookings.size()));
            JLabel upcomingLabel = new JLabel(String.format("Upcoming Flights: %d", upcomingFlights));
            JLabel spentLabel = new JLabel(String.format("Total Spent: $%.2f", totalSpent));

            // Style labels
            Font summaryFont = new Font("Segoe UI", Font.BOLD, 14);
            totalLabel.setFont(summaryFont);
            upcomingLabel.setFont(summaryFont);
            spentLabel.setFont(summaryFont);

            panel.add(totalLabel);
            panel.add(upcomingLabel);
            panel.add(spentLabel);

        } catch (FlightBookingSystemException e) {
            panel.add(new JLabel("Error loading summary"));
        }

        return panel;
    }
}
