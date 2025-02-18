package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;
import java.time.LocalDate;

/**
 * Window displaying a customer's flight bookings with a modern dark theme.
 */
public class MyFlightsWindow extends JFrame {
    
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color HEADER_BG = new Color(50, 50, 50);
    
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
        setSize(900, 600);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(DARK_BG);

        // Create table model
        String[] columns = {
            "Flight Number", "From", "To", "Date", "Status", 
            "Price", "Payment Status"
        };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        populateTableModel(model);

        // Create and style the table
        flightsTable = createStyledTable(model);
        JScrollPane scrollPane = createStyledScrollPane(flightsTable);

        // Create summary panel
        JPanel summaryPanel = createSummaryPanel();

        // Add components to frame
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void populateTableModel(DefaultTableModel model) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            for (Booking booking : customer.getBookings()) {
                Flight flight = booking.getFlight();
                String status = flight.getDepartureDate().isAfter(LocalDate.now()) 
                    ? "Upcoming" : "Past";
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
            showErrorMessage("Error loading flights: " + e.getMessage());
        }
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(HEADER_BG);
        table.setFont(new Font("Dialog", Font.PLAIN, 14));
        table.setRowHeight(35);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT_COLOR);
        header.setFont(new Font("Dialog", Font.BOLD, 14));
        
        // Add status color renderer
        table.getColumn("Status").setCellRenderer(new StatusCellRenderer());
        table.getColumn("Payment Status").setCellRenderer(new PaymentStatusRenderer());
        
        return table;
    }

    private JScrollPane createStyledScrollPane(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return scrollPane;
    }

    private JPanel createSummaryPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 20, 0));
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

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

            panel.add(createSummaryLabel(String.format("Total Flights: %d", bookings.size())));
            panel.add(createSummaryLabel(String.format("Upcoming Flights: %d", upcomingFlights)));
            panel.add(createSummaryLabel(String.format("Total Spent: $%.2f", totalSpent)));

        } catch (FlightBookingSystemException e) {
            panel.add(createSummaryLabel("Error loading summary"));
        }

        return panel;
    }

    private JLabel createSummaryLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setForeground(TEXT_COLOR);
        label.setFont(new Font("Dialog", Font.BOLD, 14));
        return label;
    }

    private void showErrorMessage(String message) {
        UIManager.put("OptionPane.background", DARK_BG);
        UIManager.put("Panel.background", DARK_BG);
        UIManager.put("OptionPane.messageForeground", TEXT_COLOR);
        JOptionPane.showMessageDialog(this, message, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Custom renderer for the Status column
     */
    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                c.setForeground("Upcoming".equals(value) ? 
                    new Color(75, 175, 80) : // Green
                    new Color(158, 158, 158)); // Gray
            }
            return c;
        }
    }

    /**
     * Custom renderer for the Payment Status column
     */
    private class PaymentStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value,
                    isSelected, hasFocus, row, column);
            
            if (!isSelected) {
                c.setForeground("Paid".equals(value) ? 
                    new Color(75, 175, 80) : // Green
                    new Color(255, 87, 34)); // Orange-Red
            }
            return c;
        }
    }
}