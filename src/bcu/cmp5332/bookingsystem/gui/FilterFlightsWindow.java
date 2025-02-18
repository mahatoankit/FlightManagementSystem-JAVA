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

/**
 * A GUI window that allows users to filter flights based on various criteria.
 * This class provides functionality to filter flights by origin, destination,
 * departure date, and price range.
 */
public class FilterFlightsWindow extends JFrame implements ActionListener {

    /** The flight booking system instance */
    private FlightBookingSystem fbs;
    /** Text field for entering origin airport */
    private JTextField originField = new JTextField(10);
    /** Text field for entering destination airport */
    private JTextField destinationField = new JTextField(10);
    /** Text field for entering departure date */
    private JTextField depDateField = new JTextField(10);
    /** Text field for entering minimum price */
    private JTextField minPriceField = new JTextField(10);
    /** Text field for entering maximum price */
    private JTextField maxPriceField = new JTextField(10);
    /** Button to apply the filter */
    private JButton filterButton = new JButton("Apply Filter");
    /** Table to display filtered results */
    private JTable resultsTable;

    // Add these color constants at the top of the class
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);

    /**
     * Constructs a new FilterFlightsWindow.
     * 
     * @param fbs The FlightBookingSystem instance to be used for filtering flights
     */
    public FilterFlightsWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    /**
     * Initializes the GUI components and sets up the window layout.
     * This method configures the look and feel, creates input fields,
     * and sets up the results table.
     */
    private void initialize() {
        setTitle("Filter Flights");
        setSize(800, 500);
        setLayout(new BorderLayout(10, 10));

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Input panel with grid layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBackground(DARKER_BG);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // Add components with proper spacing
        addInputComponent(inputPanel, "Origin:", originField, 0, gbc);
        addInputComponent(inputPanel, "Destination:", destinationField, 1, gbc);
        addInputComponent(inputPanel, "Departure Date (YYYY-MM-DD):", depDateField, 2, gbc);
        addInputComponent(inputPanel, "Min Price:", minPriceField, 3, gbc);
        addInputComponent(inputPanel, "Max Price:", maxPriceField, 4, gbc);

        // Style the filter button
        filterButton = createStyledButton("Apply Filter");
        filterButton.addActionListener(this);
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(filterButton, gbc);

        // Create and style the results table
        resultsTable = createStyledTable();
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    /**
     * Creates a styled JLabel with custom font.
     * 
     * @param text The text to be displayed in the label
     * @return A styled JLabel instance
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    /**
     * Creates a styled JButton with custom appearance.
     * 
     * @param button The JButton to be styled
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
     * Handles button click events.
     * 
     * @param e The ActionEvent triggered by the button click
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == filterButton) {
            applyFilter();
        }
    }

    /**
     * Applies the filter criteria and updates the results table.
     * This method processes the user input, validates it, and filters
     * the flights based on the specified criteria. The results are
     * displayed in the table.
     */
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
                JOptionPane.showMessageDialog(this, "Invalid departure date format", "Error",
                        JOptionPane.ERROR_MESSAGE);
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

        String[] columns = { "ID", "Flight Number", "Origin", "Destination", "Departure Date", "Dynamic Price" };
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

        // Apply consistent styling to the new data
        resultsTable.setBackground(DARKER_BG);
        resultsTable.setForeground(TEXT_COLOR);
        resultsTable.getTableHeader().setBackground(DARKER_BG);
        resultsTable.getTableHeader().setForeground(TEXT_COLOR);

        // Add row striping for better readability
        resultsTable.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? DARKER_BG : DARK_BG);
                    c.setForeground(TEXT_COLOR);
                }

                return c;
            }
        });
    }

    private void addInputComponent(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        // Label
        JLabel label = new JLabel(labelText);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        // Text field
        styleTextField(field);
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        panel.add(field, gbc);
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

    private JTable createStyledTable() {
        JTable table = new JTable();
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(DARK_BG);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setBackground(DARKER_BG);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setRowHeight(30);

        return table;
    }
}