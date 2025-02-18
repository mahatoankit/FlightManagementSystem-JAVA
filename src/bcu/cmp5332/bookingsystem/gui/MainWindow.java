package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Booking;
import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.Flight;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;

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
        setUIFont();
        initialize();
    }

    private void setUIFont() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            Font menuFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font tableFont = new Font("Segoe UI", Font.PLAIN, 13);

            // Set default fonts
            UIManager.put("Menu.font", menuFont);
            UIManager.put("MenuItem.font", menuFont);
            UIManager.put("Table.font", tableFont);
            UIManager.put("Label.font", menuFont);
            UIManager.put("Button.font", menuFont);

            // Set other UI properties
            UIManager.put("Table.rowHeight", 25);
            UIManager.put("Table.gridColor", new Color(220, 220, 220));
            UIManager.put("Table.selectionBackground", new Color(51, 122, 183));
            UIManager.put("Table.selectionForeground", Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void styleMenuBar() {
        if (menuBar != null) {
            Color menuBackground = new Color(240, 240, 240);
            menuBar.setBackground(menuBackground);

            for (int i = 0; i < menuBar.getMenuCount(); i++) {
                JMenu menu = menuBar.getMenu(i);
                if (menu != null) {
                    menu.setBackground(menuBackground);
                    menu.setBorderPainted(false);
                    styleMenuItems(menu);
                }
            }
        }
    }

    private void styleMenuItems(JMenu menu) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(Color.WHITE);
                item.setBorderPainted(false);
                item.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        item.setBackground(new Color(230, 240, 250));
                    }

                    public void mouseExited(MouseEvent e) {
                        item.setBackground(Color.WHITE);
                    }
                });
            }
        }
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

        // Create menuBar first
        menuBar = new JMenuBar();
        initMenuBar(); // This will populate the menuBar
        setJMenuBar(menuBar);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    /**
     * Initializes the main menu bar of the application.
     * Creates the menu bar, admin menu, flights menu, bookings menu, and customers
     * menu.
     * Adds the menu items to their respective menus and sets the action listeners
     * for each menu item.
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
     * The table includes the flight ID, flight number, origin, destination,
     * departure date, base price, and capacity.
     * Double-clicking on a flight will display its details.
     */
    public void displayAllFlights() {
        List<Flight> allFlights = new ArrayList<>(fbs.getAllFlights());
        List<Flight> activeFlights = allFlights.stream()
                .filter(f -> !f.isDeleted())
                .collect(Collectors.toList());
        List<Flight> deletedFlights = allFlights.stream()
                .filter(Flight::isDeleted)
                .collect(Collectors.toList());

        // Create panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);

        // Active flights panel
        JPanel activePanel = new JPanel(new BorderLayout());
        activePanel.setBorder(BorderFactory.createTitledBorder("Active Flights"));
        JTable activeTable = createFlightTable(activeFlights);
        activePanel.add(new JScrollPane(activeTable), BorderLayout.CENTER);

        // Deleted flights panel
        JPanel deletedPanel = new JPanel(new BorderLayout());
        deletedPanel.setBorder(BorderFactory.createTitledBorder("Deleted Flights"));
        JTable deletedTable = createFlightTable(deletedFlights);
        deletedPanel.add(new JScrollPane(deletedTable), BorderLayout.CENTER);

        // Add tables to split pane
        splitPane.setTopComponent(activePanel);
        splitPane.setBottomComponent(deletedPanel);

        // Add split pane to main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Refresh the main window with the new panel
        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        setTitle("Flight Booking System - All Flights");
        revalidate();
        repaint();
    }

    private JTable createFlightTable(List<Flight> flights) {
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

        JTable table = new JTable(data, columns) {
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
                        int flightId = (int) table.getValueAt(row, 0);
                        displayFlightDetails(flightId);
                    }
                }
            }
        });

        return table;
    }

    // Show detailed flight info, including passenger list.
    public void displayFlightDetails(int flightId) {
        try {
            Flight flight = fbs.getFlightByID(flightId);
            List<Customer> passengers = flight.getPassengers();

            System.out.println("DEBUG: Displaying details for flight " + flight.getFlightNumber());
            System.out.println("DEBUG: Number of passengers found: " + passengers.size());

            StringBuilder details = new StringBuilder();
            details.append("Flight Details:\n")
                    .append("Flight Number: ").append(flight.getFlightNumber()).append("\n")
                    .append("Origin: ").append(flight.getOrigin()).append("\n")
                    .append("Destination: ").append(flight.getDestination()).append("\n")
                    .append("Departure Date: ").append(flight.getDepartureDate()).append("\n")
                    .append("Capacity: ").append(flight.getCapacity()).append("\n")
                    .append("Base Price: $").append(String.format("%.2f", flight.getBasePrice())).append("\n")
                    .append("Current Passengers: ").append(passengers.size()).append("/").append(flight.getCapacity())
                    .append("\n\n")
                    .append("Passenger List:\n");

            if (passengers.isEmpty()) {
                details.append("No passengers have booked this flight.");
            } else {
                for (Customer c : passengers) {
                    details.append("• ").append(c.getName())
                            .append(" (Phone: ").append(c.getPhone())
                            .append(", Email: ").append(c.getEmail()).append(")\n");
                }
            }

            JTextArea textArea = new JTextArea(details.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setMargin(new Insets(10, 10, 10, 10));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 400));

            JOptionPane.showMessageDialog(this, scrollPane,
                    "Flight Details - " + flight.getFlightNumber(),
                    JOptionPane.INFORMATION_MESSAGE);

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error retrieving flight details: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
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
             * @param row    the row being queried
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
             * @param row    the row index of the cell being queried
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
     * Displays a window showing all customers in the system, including soft-deleted
     * customers.
     * The window shows all customer details, with a single column for the "Active"
     * status
     * which is "Yes" if the customer is active and "No" if the customer is
     * soft-deleted.
     * When a row is double-clicked, it will display the booking details for the
     * customer
     * represented by that row.
     */
    public void displayAllCustomers() {
        List<Customer> allCustomers = fbs.getAllCustomers();
        List<Customer> activeCustomers = allCustomers.stream()
                .filter(c -> !c.isDeleted())
                .collect(Collectors.toList());
        List<Customer> deletedCustomers = allCustomers.stream()
                .filter(Customer::isDeleted)
                .collect(Collectors.toList());

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(230, 230, 250);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setBorder(null);

        // Active customers panel
        JPanel activePanel = createCustomerPanel("Active Customers", activeCustomers,
                new Color(240, 255, 240), new Color(144, 238, 144));

        // Deleted customers panel
        JPanel deletedPanel = createCustomerPanel("Deleted Customers", deletedCustomers,
                new Color(255, 240, 240), new Color(255, 182, 193));

        // Add panels to split pane
        splitPane.setTopComponent(activePanel);
        splitPane.setBottomComponent(deletedPanel);

        // Add split pane to main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add statistics panel
        JPanel statsPanel = createStatsPanel(activeCustomers.size(), deletedCustomers.size());
        mainPanel.add(statsPanel, BorderLayout.SOUTH);

        // Refresh the main window
        getContentPane().removeAll();
        getContentPane().add(mainPanel);
        setTitle("Flight Booking System - Customer Management");
        revalidate();
        repaint();
    }

    private JPanel createCustomerPanel(String title, List<Customer> customers, Color bgColor1, Color bgColor2) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, bgColor1, w, h, bgColor2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Create titled border with custom font and color
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 100, 100), 1),
                title);
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create and configure table
        String[] columns = { "ID", "Name", "Phone", "Email", "Bookings" };
        Object[][] data = new Object[customers.size()][5];
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.getEmail();
            data[i][4] = c.getBookings().size();
        }

        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (comp instanceof JComponent) {
                    ((JComponent) comp).setToolTipText(String.valueOf(getValueAt(row, col)));
                }
                return comp;
            }
        };

        // Style the table
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(200, 200, 200));

        // Add mouse listener for double-click
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        int customerId = (int) table.getValueAt(row, 0);
                        showCustomerBookingDetails(customerId);
                    }
                }
            }
        });

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel(int activeCount, int deletedCount) {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setBackground(new Color(240, 240, 240));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        // Create statistics labels with custom styling
        JLabel activeLabel = new JLabel(String.format("Active Customers: %d", activeCount));
        JLabel deletedLabel = new JLabel(String.format("Deleted Customers: %d", deletedCount));
        JLabel totalLabel = new JLabel(String.format("Total Customers: %d", activeCount + deletedCount));

        Font statsFont = new Font("Segoe UI", Font.BOLD, 12);
        activeLabel.setFont(statsFont);
        deletedLabel.setFont(statsFont);
        totalLabel.setFont(statsFont);

        activeLabel.setForeground(new Color(46, 125, 50));
        deletedLabel.setForeground(new Color(198, 40, 40));
        totalLabel.setForeground(new Color(13, 71, 161));

        statsPanel.add(activeLabel);
        statsPanel.add(new JSeparator(JSeparator.VERTICAL));
        statsPanel.add(deletedLabel);
        statsPanel.add(new JSeparator(JSeparator.VERTICAL));
        statsPanel.add(totalLabel);

        return statsPanel;
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
                try {
                    int custId = (int) currentTable.getValueAt(selectedRow, 0);
                    Customer customer = fbs.getCustomerByID(custId);

                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Are you sure you want to delete customer: " + customer.getName() + "?\n" +
                                    "This will also cancel all their active bookings.",
                            "Confirm Deletion",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.WARNING_MESSAGE);

                    if (confirm == JOptionPane.YES_OPTION) {
                        fbs.removeCustomer(custId);
                        FlightBookingSystemData.store(fbs);
                        JOptionPane.showMessageDialog(
                                this,
                                "Customer deleted successfully",
                                "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        displayAllCustomers();
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Error deleting customer: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a customer to delete",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE);
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
        // Add debug print
        System.out.println("DEBUG: Current flights in system: " + fbs.getFlights().size());
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
     *          no-op.
     * 
     * 
     * @Override
     *           public void actionPerformed(ActionEvent e) {
     *           // Not used directly; individual menu items have their own
     *           listeners.
     *           }
     * 
     *           /**
     *           The main entry point for the application. It initializes the flight
     *           booking system
     *           by loading data and launching the login window on the Event
     *           Dispatch Thread.
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
