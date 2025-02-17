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
        /**
         * Listens for the window closing event and stores the current state of the system to a file before exiting.
         * If there is an error storing the data, a message dialog will appear with the error message.
         * @param e the window closing event
         */
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

    /**
     * Applies custom UI settings to the application, such as font sizes, font styles, and panel background colors.
     * This method is called once in the constructor of the MainWindow class.
     */
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

    /**
     * Initializes the main window of the application.
     * Sets the window title, size, and default close operation.
     * Calls the initMenuBar method to create the menu bar.
     * Finally, sets the window to be visible.
     */
    private void initialize() {
        setTitle("Flight Booking Management System");
        setSize(1000, 600);
        initMenuBar();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Initializes the main menu bar of the application.
     * Creates the menu bar, admin menu, flights menu, bookings menu, and customers menu.
     * Adds the menu items to their respective menus and sets the action listeners for each menu item.
     * Finally, sets the menu bar to the window.
     */
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
        flightsViewUpcoming.addActionListener(e -> displayUpcomingFlights());
        flightsViewAll = new JMenuItem("View All Flights");
        flightsViewAll.setToolTipText("View all flights including past flights");
    
        flightsViewAll.addActionListener(e -> displayAllFlights());
        flightsAdd = new JMenuItem("Add New Flight");
        flightsAdd.setToolTipText("Add a new flight (Admin only)");
        flightsAdd.addActionListener(e -> new AddFlightWindow(this));
        flightsDel = new JMenuItem("Delete Flight");
        flightsDel.setToolTipText("Delete a flight (Admin only)");
        flightsDel.addActionListener(e -> deleteSelectedFlight());
        JMenuItem flightsFilter = new JMenuItem("Filter Flights");
        flightsFilter.setToolTipText("Filter flights by criteria");
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

        bookingsView.addActionListener(e -> displayBookings());
        bookingsIssue = new JMenuItem("New Booking");
        bookingsIssue.setToolTipText("Create a new booking");
 
        bookingsIssue.addActionListener(e -> new AddBookingWindow(this));
        bookingsUpdate = new JMenuItem("Update Booking");
        bookingsUpdate.setToolTipText("Update an existing booking");

        bookingsUpdate.addActionListener(e -> new UpdateBookingWindow(this));
        bookingsCancel = new JMenuItem("Cancel Booking");
        bookingsCancel.setToolTipText("Cancel an existing booking");

        bookingsCancel.addActionListener(e -> new CancelBookingWindow(this));
        bookingsViewAllCombined = new JMenuItem("View All Bookings");
        bookingsViewAllCombined.setToolTipText("View all bookings (active & cancelled)");

        bookingsViewAllCombined.addActionListener(e -> displayAllBookings());
        bookingsPayment = new JMenuItem("Process Payment");
        bookingsPayment.setToolTipText("Process payment for a booking");

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

            viewActive.addActionListener(e -> displayActiveCustomers());
            JMenuItem viewAll = new JMenuItem("View All Customers");
            viewAll.setToolTipText("Show all registered customers including deleted ones");

            viewAll.addActionListener(e -> displayAllCustomers());
            JMenuItem addCustomer = new JMenuItem("Add New Customer");
            addCustomer.setToolTipText("Add a new customer");

            addCustomer.addActionListener(e -> new AddCustomerWindow(this));
            JMenuItem updateCustomer = new JMenuItem("Update Customer");
            updateCustomer.setToolTipText("Update an existing customer");

            updateCustomer.addActionListener(e -> new UpdateCustomerWindow(this));
            JMenuItem deleteCustomer = new JMenuItem("Delete Customer");
            deleteCustomer.setToolTipText("Delete (soft-delete) a customer");

            deleteCustomer.addActionListener(e -> deleteSelectedCustomer());
            customersMenu.add(viewActive);
            customersMenu.add(viewAll);
            customersMenu.add(addCustomer);
            customersMenu.add(updateCustomer);
            customersMenu.add(deleteCustomer);
        } else {
            JMenuItem myDetails = new JMenuItem("My Details");
            myDetails.setToolTipText("View your account details");

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
            /**
             * Handles double-clicks on the table by selecting the flight and
             * displaying its details.
             * 
             * @param e the mouse event
             */
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

    /**
     * Displays all flights in the system, including deleted flights.
     * The table includes the flight ID, flight number, origin, destination, departure date, base price, and capacity.
     * Double-clicking on a flight will display its details.
     */
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
/**
 * Handles double-click events on the flights table.
 * If a row is double-clicked, retrieves the flight ID from the selected row
 * and displays the flight details by calling displayFlightDetails().
 * If no row is selected on double-click, logs a message indicating that
 * no row was selected.
 *
 * @param e The MouseEvent triggered by the double-click action
 */

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

            /**
             * Disables cell editing for the table.
             * 
             * @param row   the row being queried
             * @param column the column being queried
             * @return false to indicate that cells are not editable
             */
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

/**
 * Determines whether a cell in the table is editable.
 * This implementation ensures that no cells are editable.
 *
 * @param row the row index of the cell being queried
 * @param column the column index of the cell being queried
 * @return false always, indicating cells are not editable
 */

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.addMouseListener(new MouseAdapter() {
            /**
             * Handles double-clicks on the table rows.
             * 
             * If a row is double-clicked, it will display the booking details
             * for the customer represented by that row.
             * 
             * @param e the MouseEvent representing the double-click
             */
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

    /**
     * Displays a window showing all customers in the system, including soft-deleted customers.
     * The window shows all customer details, with a single column for the "Active" status
     * which is "Yes" if the customer is active and "No" if the customer is soft-deleted.
     * When a row is double-clicked, it will display the booking details for the customer
     * represented by that row.
     */
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
/**
 * Handles double-click events on table rows.
 * 
 * When a row is double-clicked, it retrieves the customer ID
 * from the selected row and displays the booking details for
 * the corresponding customer. If no row is selected on double-click,
 * a message is printed indicating that no row was selected.
 * 
 * @param e the MouseEvent representing the double-click
 */

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

    /**
     * Sets the ID of the currently logged-in customer.
     * 
     * @param id the logged-in customer's ID
     */
    public void setLoggedInCustomerId(int id) {
        this.loggedInCustomerId = id;
    }


    /**
     * Returns the ID of the currently logged-in customer.
     * 
     * @return the logged-in customer's ID, or null if no customer is logged in
     */

    public Integer getLoggedInCustomerId() {
        return loggedInCustomerId;
    }


    /**
     * Refreshes the bookings display to show the current bookings.
     * 
     * This method is used to re-display the bookings after a booking is added,
     * updated, or deleted. It simply calls the displayBookings() method to
     * refresh the display.
     */
    public void refreshBookingsDisplay() {
        displayBookings();
    }
    /**
     * This method is not used directly; individual menu items have their own
     * listeners and handle their own events. This implementation is a
     * 
     * @param e The ActionEvent triggered by the user
     * no-op.


    @Override
    public void actionPerformed(ActionEvent e) {
        // Not used directly; individual menu items have their own listeners.
    }

/**
 * The main entry point for the application. It initializes the flight booking system
 * by loading data and launching the login window on the Event Dispatch Thread.
 *
 * @param args command-line arguments (not used)
 */

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

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }
}
