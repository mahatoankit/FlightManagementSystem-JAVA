package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.ArrayList;

public class FilterFlightsWindow extends JFrame implements ActionListener {

    private FlightBookingSystem fbs;
    private JTextField originField = new JTextField(10);
    private JTextField destinationField = new JTextField(10);
    private JTextField depDateField = new JTextField(10);
    private JTextField minPriceField = new JTextField(10);
    private JTextField maxPriceField = new JTextField(10);
    private JButton filterButton = new JButton("Apply Filter");
    private JTable resultsTable;

    public FilterFlightsWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Filter Flights");
        setSize(600, 400);
        setLayout(new BorderLayout(5, 5));

        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(new Color(240, 240, 240));

        inputPanel.add(createStyledLabel("Origin:"));
        inputPanel.add(originField);
        inputPanel.add(createStyledLabel("Destination:"));
        inputPanel.add(destinationField);
        inputPanel.add(createStyledLabel("Departure Date (YYYY-MM-DD):"));
        inputPanel.add(depDateField);
        inputPanel.add(createStyledLabel("Min Price:"));
        inputPanel.add(minPriceField);
        inputPanel.add(createStyledLabel("Max Price:"));
        inputPanel.add(maxPriceField);
        inputPanel.add(new JLabel(""));
        inputPanel.add(createStyledButton(filterButton));

        filterButton.addActionListener(this);

        resultsTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setLocationRelativeTo(null);
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
        if (e.getSource() == filterButton) {
            applyFilter();
        }
    }

    private void applyFilter() {
        String origin = originField.getText().trim();
        String destination = destinationField.getText().trim();
        String depDateStr = depDateField.getText().trim();
        String minPriceStr = minPriceField.getText().trim();
        String maxPriceStr = maxPriceField.getText().trim();

        LocalDate filterDate = null;
        if (!depDateStr.isEmpty()) {
            try {
                filterDate = LocalDate.parse(depDateStr);
            } catch (DateTimeParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid departure date format", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Double minPrice = null;
        if (!minPriceStr.isEmpty()) {
            try {
                minPrice = Double.parseDouble(minPriceStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid minimum price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Double maxPrice = null;
        if (!maxPriceStr.isEmpty()) {
            try {
                maxPrice = Double.parseDouble(maxPriceStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid maximum price", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        List<Flight> filtered = new ArrayList<>();
        for (Flight flight : fbs.getFlights()) {
            boolean matches = true;
            if (!origin.isEmpty() && !flight.getOrigin().equalsIgnoreCase(origin)) {
                matches = false;
            }
            if (!destination.isEmpty() && !flight.getDestination().equalsIgnoreCase(destination)) {
                matches = false;
            }
            if (filterDate != null && !flight.getDepartureDate().isEqual(filterDate)) {
                matches = false;
            }
            double dynamicPrice = flight.calculatePrice(LocalDate.now());
            if (minPrice != null && dynamicPrice < minPrice) {
                matches = false;
            }
            if (maxPrice != null && dynamicPrice > maxPrice) {
                matches = false;
            }
            if (matches) {
                filtered.add(flight);
            }
        }

        String[] columns = {"ID", "Flight Number", "Origin", "Destination", "Departure Date", "Dynamic Price"};
        Object[][] data = new Object[filtered.size()][6];
        for (int i = 0; i < filtered.size(); i++) {
            Flight f = filtered.get(i);
            data[i][0] = f.getId();
            data[i][1] = f.getFlightNumber();
            data[i][2] = f.getOrigin();
            data[i][3] = f.getDestination();
            data[i][4] = f.getDepartureDate();
            data[i][5] = f.calculatePrice(LocalDate.now());
        }

        resultsTable.setModel(new javax.swing.table.DefaultTableModel(data, columns));
    }
}