package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MainWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;
    private JTable currentTable;
    private boolean isAdmin = true;
    private Integer loggedInCustomerId = null;

    private JMenuBar menuBar;
    private JMenu adminMenu, flightsMenu, bookingsMenu, customersMenu;
    private JMenuItem adminExit;
    private JMenuItem flightsViewUpcoming, flightsViewAll, flightsAdd, flightsDel;
    private JMenuItem bookingsView, bookingsIssue, bookingsUpdate, bookingsCancel, bookingsViewAllCombined;
    private JMenuItem bookingsPayment; // Add this as a class field

    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        applyCustomUI();
        initialize();
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    FlightBookingSystemData.store(fbs);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainWindow.this,
                            "Error saving data: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                } finally {
                    System.exit(0);
                }
            }
        });
    }

    private void applyCustomUI() {
        UIManager.put("Label.font", new Font("SansSerif", Font.PLAIN, 18));
        UIManager.put("Button.font", new Font("SansSerif", Font.BOLD, 18));
        UIManager.put("TextField.font", new Font("SansSerif", Font.PLAIN, 18));
        UIManager.put("Table.font", new Font("SansSerif", Font.PLAIN, 18));
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Panel.background", Color.WHITE);
    }

    // Helper method to load and scale icons from file paths
    private ImageIcon loadScaledIcon(String path, int width, int height) {
        File f = new File(path);
        if (!f.exists()) {
            System.err.println("Icon file not found: " + path);
        }
        ImageIcon icon = new ImageIcon(path);
        Image scaledImage = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private void initialize() {
        setTitle("Flight Booking Management System");
        setSize(1000, 600);
        initMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        // Admin Menu
        adminMenu = new JMenu("Admin");
        adminExit = new JMenuItem("Exit");
        adminExit.setToolTipText("Exit the application");
        adminExit.setIcon(loadScaledIcon("resources/icons/exit.png", 24, 24));
        adminExit.addActionListener(e -> {
            try {
                exitApplication();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainWindow.this,
                        "Error exiting application: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
        adminMenu.add(adminExit);
        menuBar.add(adminMenu);

        // Flights Menu
        flightsMenu = new JMenu("Flights");
        flightsViewUpcoming = new JMenuItem("View Upcoming Flights");
        flightsViewUpcoming.setToolTipText("View flights that have not departed");
        flightsViewUpcoming.setIcon(loadScaledIcon("resources/icons/view.png", 24, 24));
        flightsViewUpcoming.addActionListener(e -> displayUpcomingFlights());
        flightsViewAll = new JMenuItem("View All Flights");
        flightsViewAll.setToolTipText("View all flights including past flights");
        flightsViewAll.setIcon(loadScaledIcon("resources/icons/view_all.png", 24, 24));
        flightsViewAll.addActionListener(e -> displayAllFlights());
        flightsAdd = new JMenuItem("Add New Flight");
        flightsAdd.setToolTipText("Add a new flight (Admin only)");
        flightsAdd.setIcon(loadScaledIcon("resources/icons/add.png", 24, 24));
        flightsAdd.addActionListener(e -> new AddFlightWindow(this));
        flightsDel = new JMenuItem("Delete Flight");
        flightsDel.setToolTipText("Delete a flight (Admin only)");
        flightsDel.setIcon(loadScaledIcon("resources/icons/delete.png", 24, 24));
        flightsDel.addActionListener(e -> deleteSelectedFlight());
        JMenuItem flightsFilter = new JMenuItem("Filter Flights");
        flightsFilter.setToolTipText("Filter flights by criteria");
        flightsFilter.setIcon(loadScaledIcon("resources/icons/filter.png", 24, 24));
        flightsFilter.addActionListener(e -> new FilterFlightsWindow(fbs));
        flightsMenu.add(flightsViewUpcoming);
        flightsMenu.add(flightsViewAll);
        flightsMenu.add(flightsAdd);
        flightsMenu.add(flightsDel);
        flightsMenu.add(flightsFilter);
        menuBar.add(flightsMenu);

        // Bookings Menu
        bookingsMenu = new JMenu("Bookings");
        bookingsView = new JMenuItem("View Bookings");
        bookingsView.setToolTipText("View current bookings");
        bookingsView.setIcon(loadScaledIcon("resources/icons/view.png", 24, 24));
        bookingsView.addActionListener(e -> displayBookings());
        bookingsIssue = new JMenuItem("New Booking");
        bookingsIssue.setToolTipText("Create a new booking");
        bookingsIssue.setIcon(loadScaledIcon("resources/icons/add.png", 24, 24));
        bookingsIssue.addActionListener(e -> new AddBookingWindow(this));
        bookingsUpdate = new JMenuItem("Update Booking");
        bookingsUpdate.setToolTipText("Update an existing booking");
        bookingsUpdate.setIcon(loadScaledIcon("resources/icons/update.png", 24, 24));
        bookingsUpdate.addActionListener(e -> new UpdateBookingWindow(this));
        bookingsCancel = new JMenuItem("Cancel Booking");
        bookingsCancel.setToolTipText("Cancel an existing booking");
        bookingsCancel.setIcon(loadScaledIcon("resources/icons/cancel.png", 24, 24));
        bookingsCancel.addActionListener(e -> new CancelBookingWindow(this));
        bookingsViewAllCombined = new JMenuItem("View All Bookings");
        bookingsViewAllCombined.setToolTipText("View all bookings (active & cancelled)");
        bookingsViewAllCombined.setIcon(loadScaledIcon("resources/icons/view_all.png", 24, 24));
        bookingsViewAllCombined.addActionListener(e -> displayAllBookings());
        bookingsPayment = new JMenuItem("Process Payment");
        bookingsPayment.setToolTipText("Process payment for a booking");
        bookingsPayment.setIcon(loadScaledIcon("resources/icons/payment.png", 24, 24));
        bookingsPayment.addActionListener(e -> processSelectedBookingPayment());
        bookingsMenu.add(bookingsView);
        bookingsMenu.add(bookingsIssue);
        bookingsMenu.add(bookingsUpdate);
        bookingsMenu.add(bookingsCancel);
        bookingsMenu.add(bookingsViewAllCombined);
        bookingsMenu.add(bookingsPayment);
        menuBar.add(bookingsMenu);

        // Customers Menu
        customersMenu = new JMenu("Customers");
        if (isAdmin) {
            JMenuItem viewActive = new JMenuItem("View Active Customers");
            viewActive.setToolTipText("Show only active (non-deleted) customers");
            viewActive.setIcon(loadScaledIcon("resources/icons/view.png", 18, 18));
            viewActive.addActionListener(e -> displayActiveCustomers());
            JMenuItem viewAll = new JMenuItem("View All Customers");
            viewAll.setToolTipText("Show all registered customers including deleted ones");
            viewAll.setIcon(loadScaledIcon("resources/icons/view_all.png", 18, 18));
            viewAll.addActionListener(e -> displayAllCustomers());
            JMenuItem addCustomer = new JMenuItem("Add New Customer");
            addCustomer.setToolTipText("Add a new customer");
            addCustomer.setIcon(loadScaledIcon("resources/icons/add.png", 18, 18));
            addCustomer.addActionListener(e -> new AddCustomerWindow(this));
            JMenuItem updateCustomer = new JMenuItem("Update Customer");
            updateCustomer.setToolTipText("Update an existing customer");
            updateCustomer.setIcon(loadScaledIcon("resources/icons/update.png", 18, 18));
            updateCustomer.addActionListener(e -> new UpdateCustomerWindow(this));
            JMenuItem deleteCustomer = new JMenuItem("Delete Customer");
            deleteCustomer.setToolTipText("Delete (soft-delete) a customer");
            deleteCustomer.setIcon(loadScaledIcon("resources/icons/delete.png", 18, 18));
            deleteCustomer.addActionListener(e -> deleteSelectedCustomer());
            customersMenu.add(viewActive);
            customersMenu.add(viewAll);
            customersMenu.add(addCustomer);
            customersMenu.add(updateCustomer);
            customersMenu.add(deleteCustomer);
        } else {
            JMenuItem myDetails = new JMenuItem("My Details");
            myDetails.setToolTipText("View your account details");
            myDetails.setIcon(loadScaledIcon("resources/icons/details.png", 24, 24));
            myDetails.addActionListener(e -> displayCustomerDetails(loggedInCustomerId));
            customersMenu.add(myDetails);
        }
        menuBar.add(customersMenu);

        setJMenuBar(menuBar);
    }

    // --- Refresh Table with debug print ---
    private void refreshTable(JTable table, String title) {
        System.out.println("Refreshing table: " + title);
        getContentPane().removeAll();
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setTitle("Flight Booking System - " + title);
        revalidate();
        repaint();
        currentTable = table;
    }

    // --- Display Methods ---
    public void displayUpcomingFlights() {
        List<Flight> flights = fbs.getFlights();
        String[] columns = { "ID", "Flight Number", "Origin", "Destination", "Departure Date", "Base Price",
                "Capacity" };
        Object[][] data = new Object[flights.size()][7];
        for (int i = 0; i < flights.size(); i++) {
            Flight flight = flights.get(i);
            data[i][0] = flight.getId();
            data[i][1] = flight.getFlightNumber();
            data[i][2] = flight.getOrigin();
            data[i][3] = flight.getDestination();
            data[i][4] = flight.getDepartureDate();
            data[i][5] = flight.getBasePrice();
            data[i][6] = flight.getCapacity();
        }
        JTable table = new JTable(data, columns);
        refreshTable(table, "Upcoming Flights");
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int flightId = (int) table.getValueAt(row, 0);
                        System.out.println("Double-clicked flight ID: " + flightId);
                        displayFlightDetails(flightId);
                    } else {
                        System.out.println("No row selected on double-click.");
                    }
                }
            }
        });
    }

    public void displayAllFlights() {
        List<Flight> allFlights = new ArrayList<>(fbs.getAllFlights());
        String[] columns = { "ID", "Flight Number", "Origin", "Destination", "Departure Date", "Base Price",
                "Capacity" };
        Object[][] data = new Object[allFlights.size()][7];
        for (int i = 0; i < allFlights.size(); i++) {
            Flight flight = allFlights.get(i);
            data[i][0] = flight.getId();
            data[i][1] = flight.getFlightNumber();
            data[i][2] = flight.getOrigin();
            data[i][3] = flight.getDestination();
            data[i][4] = flight.getDepartureDate();
            data[i][5] = flight.getBasePrice();
            data[i][6] = flight.getCapacity();
        }
        JTable table = new JTable(data, columns);
        refreshTable(table, "All Flights");
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int flightId = (int) table.getValueAt(row, 0);
                        System.out.println("Double-clicked flight ID: " + flightId);
                        displayFlightDetails(flightId);
                    } else {
                        System.out.println("No row selected on double-click.");
                    }
                }
            }
        });
    }

    // Show detailed flight info, including passenger list.
    public void displayFlightDetails(int flightId) {
        try {
            Flight flight = fbs.getFlightByID(flightId);
            StringBuilder details = new StringBuilder();
            details.append("Flight Details:\n")
                    .append("Flight Number: ").append(flight.getFlightNumber()).append("\n")
                    .append("Origin: ").append(flight.getOrigin()).append("\n")
                    .append("Destination: ").append(flight.getDestination()).append("\n")
                    .append("Departure Date: ").append(flight.getDepartureDate()).append("\n")
                    .append("Capacity: ").append(flight.getCapacity()).append("\n")
                    .append("Base Price: $").append(flight.getBasePrice()).append("\n\n")
                    .append("Passengers:\n");
            List<Customer> passengers = flight.getPassengers();
            if (passengers.isEmpty()) {
                details.append("No passengers have booked this flight.");
            } else {
                for (Customer c : passengers) {
                    details.append(c.getName()).append(" (").append(c.getPhone()).append(")\n");
                }
            }
            JOptionPane.showMessageDialog(this, details.toString(), "Flight Details", JOptionPane.INFORMATION_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error retrieving flight details: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Bookings ---
    public void displayBookings() {
        List<Booking> bookingsList = fbs.getBookings();
        String[] columns = { "Booking ID", "Customer", "Flight", "Booking Date", "Fee", "Payment Status" };
        Object[][] data = new Object[bookingsList.size()][6];
        for (int i = 0; i < bookingsList.size(); i++) {
            Booking booking = bookingsList.get(i);
            data[i][0] = booking.getId();
            data[i][1] = booking.getCustomer().getName();
            data[i][2] = booking.getFlight().getFlightNumber();
            data[i][3] = booking.getBookingDate();
            data[i][4] = booking.getBookingFee();
            data[i][5] = booking.isPaymentProcessed() ? "Paid" : "Pending";
        }
        JTable table = new JTable(data, columns) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTable(table, isAdmin ? "All Bookings" : "My Bookings");
    }

    // New: Display all bookings (active and cancelled)
    public void displayAllBookings() {
        List<Booking> active = fbs.getBookings();
        List<Booking> cancelled = fbs.getCancelledBookings();
        List<Booking> all = new ArrayList<>();
        all.addAll(active);
        all.addAll(cancelled);
        String[] columns = { "Booking ID", "Customer", "Flight", "Booking Date", "Fee", "Status" };
        Object[][] data = new Object[all.size()][6];
        for (int i = 0; i < all.size(); i++) {
            Booking booking = all.get(i);
            data[i][0] = booking.getId();
            data[i][1] = booking.getCustomer().getName();
            data[i][2] = booking.getFlight().getFlightNumber();
            data[i][3] = booking.getBookingDate();
            data[i][4] = booking.getBookingFee();
            data[i][5] = active.contains(booking) ? "Active" : "Cancelled";
        }
        JTable table = new JTable(data, columns) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        refreshTable(table, isAdmin ? "All Bookings" : "My Bookings");
    }

    // --- Customers ---
    public void displayActiveCustomers() {
        List<Customer> active = fbs.getCustomers();
        String[] columns = { "ID", "Name", "Phone", "Email" };
        Object[][] data = new Object[active.size()][4];
        for (int i = 0; i < active.size(); i++) {
            Customer c = active.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.getEmail();
        }
        JTable table = new JTable(data, columns) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int customerId = (int) table.getValueAt(row, 0);
                        System.out.println("Double-clicked active customer ID: " + customerId);
                        showCustomerBookingDetails(customerId);
                    } else {
                        System.out.println("No row selected on double-click.");
                    }
                }
            }
        });
        refreshTable(table, "Active Customers");
    }

    public void displayAllCustomers() {
        List<Customer> all = fbs.getAllCustomers();
        String[] columns = { "ID", "Name", "Phone", "Email", "Active" };
        Object[][] data = new Object[all.size()][5];
        for (int i = 0; i < all.size(); i++) {
            Customer c = all.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.getEmail();
            data[i][4] = c.isDeleted() ? "No" : "Yes";
        }
        JTable table = new JTable(data, columns) {
            /**
             * 
             */
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int customerId = (int) table.getValueAt(row, 0);
                        System.out.println("Double-clicked all customer ID: " + customerId);
                        showCustomerBookingDetails(customerId);
                    } else {
                        System.out.println("No row selected on double-click.");
                    }
                }
            }
        });
        refreshTable(table, "All Customers");
    }

    // New: Show detailed booking info for a customer.
    public void showCustomerBookingDetails(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Booking> bookings = customer.getBookings();
            if (bookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No bookings found for " + customer.getName(),
                        "Customer Details", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            StringBuilder details = new StringBuilder();
            details.append("Booking Details for ").append(customer.getName()).append(":\n\n");
            for (Booking b : bookings) {
                Flight flight = b.getFlight();
                details.append("Flight Number: ").append(flight.getFlightNumber()).append("\n")
                        .append("Origin: ").append(flight.getOrigin()).append("\n")
                        .append("Destination: ").append(flight.getDestination()).append("\n")
                        .append("Departure Date: ").append(flight.getDepartureDate()).append("\n")
                        .append("Price: $").append(b.getBookingFee()).append("\n\n");
            }
            JOptionPane.showMessageDialog(this, details.toString(), "Customer Booking Details",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ---Customer Details---
    public void displayCustomerDetails(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            String[] columns = { "ID", "Name", "Phone", "Email", "Bookings" };
            Object[][] data = new Object[1][5];
            data[0][0] = customer.getId();
            data[0][1] = customer.getName();
            data[0][2] = customer.getPhone();
            data[0][3] = customer.getEmail();
            data[0][4] = customer.getBookings().size();
            refreshTable(new JTable(data, columns), "My Details");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // --- Deletion Methods ---
    private void deleteSelectedFlight() {
        if (currentTable != null) {
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int flightId = (int) currentTable.getValueAt(selectedRow, 0);
                try {
                    Flight flight = fbs.getFlightByID(flightId);
                    int confirm = JOptionPane.showConfirmDialog(this,
                            "Delete flight " + flight.getFlightNumber() + "?", "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        fbs.removeFlight(flightId);
                        FlightBookingSystemData.store(fbs);
                        displayUpcomingFlights();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting flight: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void deleteSelectedCustomer() {
        if (currentTable != null) {
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int custId = (int) currentTable.getValueAt(selectedRow, 0);
                try {
                    fbs.removeCustomer(custId);
                    FlightBookingSystemData.store(fbs);
                    displayAllCustomers();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error deleting customer: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private void processSelectedBookingPayment() {
        if (currentTable != null) {
            int selectedRow = currentTable.getSelectedRow();
            if (selectedRow >= 0) {
                int bookingId = (int) currentTable.getValueAt(selectedRow, 0);
                try {
                    Booking booking = fbs.getBookingByID(bookingId);
                    if (booking.isPaymentProcessed()) {
                        JOptionPane.showMessageDialog(this,
                                "Payment has already been processed for this booking.",
                                "Payment Status",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        new PaymentWindow(this, booking);
                    }
                } catch (FlightBookingSystemException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Error processing payment: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Please select a booking to process payment.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    private void exitApplication() throws Exception {
        try {
            FlightBookingSystemData.store(fbs);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving data: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            System.exit(0);
        }
    }

    public FlightBookingSystem getFlightBookingSystem() {
        return fbs;
    }

    public void setAdminMode(boolean isAdmin) {
        this.isAdmin = isAdmin;
        if (!isAdmin) {
            adminMenu.setVisible(false);
            flightsAdd.setEnabled(false);
            flightsDel.setEnabled(false);
            customersMenu.removeAll();
            JMenuItem myDetails = new JMenuItem("My Details");
            myDetails.addActionListener(e -> displayCustomerDetails(loggedInCustomerId));
            customersMenu.add(myDetails);
        } else {
            adminMenu.setVisible(true);
            flightsAdd.setEnabled(true);
            flightsDel.setEnabled(true);
            customersMenu.removeAll();
            JMenuItem viewActive = new JMenuItem("View Active Customers");
            viewActive.addActionListener(e -> displayActiveCustomers());
            JMenuItem viewAll = new JMenuItem("View All Customers");
            viewAll.addActionListener(e -> displayAllCustomers());
            JMenuItem addCustomer = new JMenuItem("Add New Customer");
            addCustomer.addActionListener(e -> new AddCustomerWindow(this));
            JMenuItem updateCustomer = new JMenuItem("Update Customer");
            updateCustomer.addActionListener(e -> new UpdateCustomerWindow(this));
            JMenuItem deleteCustomer = new JMenuItem("Delete Customer");
            deleteCustomer.addActionListener(e -> deleteSelectedCustomer());
            customersMenu.add(viewActive);
            customersMenu.add(viewAll);
            customersMenu.add(addCustomer);
            customersMenu.add(updateCustomer);
            customersMenu.add(deleteCustomer);
        }
    }

    public void setLoggedInCustomerId(int id) {
        this.loggedInCustomerId = id;
    }

    public Integer getLoggedInCustomerId() {
        return loggedInCustomerId;
    }

    public void refreshBookingsDisplay() {
        displayBookings();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used directly; individual menu items have their own listeners.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlightBookingSystem fbs = bcu.cmp5332.bookingsystem.data.FlightBookingSystemData.load();
                new LoginWindow(fbs);
            } catch (Exception ex) {
                System.err.println("Failed to initialize system: " + ex.getMessage());
            }
        });
    }
}
