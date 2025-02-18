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
import java.time.LocalDate;
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

    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);
    private static final Color WARNING_COLOR = new Color(255, 152, 0);
    private static final Color ERROR_COLOR = new Color(255, 87, 34);

    public MainWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        setUIFont();
        initialize();
        displayWelcomeScreen(); // Add this line
    }

    private void setUIFont() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            // Base fonts
            Font baseFont = new Font("Segoe UI", Font.PLAIN, 14);
            Font boldFont = baseFont.deriveFont(Font.BOLD);

            // Set dark theme colors
            UIManager.put("Panel.background", DARK_BG);
            UIManager.put("MenuBar.background", DARKER_BG);
            UIManager.put("Menu.background", DARKER_BG);
            UIManager.put("MenuItem.background", DARKER_BG);
            UIManager.put("Table.background", DARKER_BG);
            UIManager.put("Table.foreground", TEXT_COLOR);
            UIManager.put("TableHeader.background", DARKER_BG);
            UIManager.put("TableHeader.foreground", TEXT_COLOR);

            // Text colors
            UIManager.put("Label.foreground", TEXT_COLOR);
            UIManager.put("Menu.foreground", TEXT_COLOR);
            UIManager.put("MenuItem.foreground", TEXT_COLOR);

            // Selection colors
            UIManager.put("Table.selectionBackground", ACCENT_COLOR);
            UIManager.put("Table.selectionForeground", Color.WHITE);

            // Dialog colors
            UIManager.put("OptionPane.background", DARK_BG);
            UIManager.put("OptionPane.messageForeground", TEXT_COLOR);

            // Fonts
            UIManager.put("Menu.font", boldFont);
            UIManager.put("MenuItem.font", baseFont);
            UIManager.put("Table.font", baseFont);
            UIManager.put("TableHeader.font", boldFont);
            UIManager.put("Label.font", baseFont);

            // Component styling
            UIManager.put("Table.rowHeight", 30);
            UIManager.put("Table.intercellSpacing", new Dimension(10, 5));
            UIManager.put("Table.gridColor", DARKER_BG);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JTable createStyledTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (!isRowSelected(row)) {
                    comp.setBackground(DARKER_BG);
                    comp.setForeground(TEXT_COLOR);
                }
                return comp;
            }
        };

        // Style improvements
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(DARK_BG);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(DARKER_BG);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        return table;
    }

    private void styleMenuBar() {
        menuBar.setBackground(DARKER_BG);
        menuBar.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Component[] components = menuBar.getComponents();
        for (Component comp : components) {
            if (comp instanceof JMenu) {
                JMenu menu = (JMenu) comp;
                menu.setBackground(DARKER_BG);
                menu.setForeground(TEXT_COLOR);
                menu.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                styleMenuItems(menu);
            }
        }
    }

    private void styleMenuItems(JMenu menu) {
        for (int i = 0; i < menu.getItemCount(); i++) {
            JMenuItem item = menu.getItem(i);
            if (item != null) {
                item.setBackground(DARKER_BG);
                item.setForeground(TEXT_COLOR);
                item.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

                // Hover effect
                item.addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) {
                        item.setBackground(ACCENT_COLOR);
                        item.setForeground(Color.WHITE);
                    }

                    public void mouseExited(MouseEvent e) {
                        item.setBackground(DARKER_BG);
                        item.setForeground(TEXT_COLOR);
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

    private void displayWelcomeScreen() {
        JPanel welcomePanel = new JPanel(new BorderLayout(20, 20)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, DARK_BG, getWidth(), getHeight(), DARKER_BG);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome to Flight Booking System", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        welcomeLabel.setForeground(TEXT_COLOR);

        // User info
        String userType = isAdmin ? "Administrator" : "Customer";
        String userName = "";
        if (!isAdmin && loggedInCustomerId != null) {
            try {
                userName = fbs.getCustomerByID(loggedInCustomerId).getName();
            } catch (Exception e) {
                userName = "Unknown";
            }
        }

        JLabel userLabel = new JLabel("Logged in as: " + userType + (userName.isEmpty() ? "" : " - " + userName),
                SwingConstants.CENTER);
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        userLabel.setForeground(TEXT_COLOR);

        // Quick stats panel
        JPanel statsPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        statsPanel.setOpaque(false);

        // Add system statistics
        addStatsLabel(statsPanel, "Total Flights", fbs.getFlights().size());
        addStatsLabel(statsPanel, "Total Customers", fbs.getCustomers().size());
        addStatsLabel(statsPanel, "Total Bookings", fbs.getBookings().size());

        // Layout components
        JPanel centerPanel = new JPanel(new BorderLayout(20, 20));
        centerPanel.setOpaque(false);
        centerPanel.add(welcomeLabel, BorderLayout.NORTH);
        centerPanel.add(userLabel, BorderLayout.CENTER);
        centerPanel.add(statsPanel, BorderLayout.SOUTH);

        welcomePanel.add(centerPanel, BorderLayout.CENTER);

        // Refresh main window
        getContentPane().removeAll();
        getContentPane().add(welcomePanel);
        revalidate();
        repaint();
    }

    private void addStatsLabel(JPanel panel, String label, int value) {
        JLabel statsLabel = new JLabel(label + ": " + value, SwingConstants.CENTER);
        statsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        statsLabel.setForeground(TEXT_COLOR);
        panel.add(statsLabel);
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
        flightsViewUpcoming.setToolTipText("View flights that have not departed yet");
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

        // Create panel with BorderLayout and dark theme
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, DARK_BG, w, h, DARKER_BG);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create split pane with dark theme
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setBackground(DARKER_BG);
        splitPane.setBorder(null);

        // Active flights panel
        JPanel activePanel = new JPanel(new BorderLayout());
        TitledBorder activeBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_COLOR),
                "Active Flights");
        activeBorder.setTitleColor(TEXT_COLOR);
        activeBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        activePanel.setBorder(BorderFactory.createCompoundBorder(
                activeBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        activePanel.setBackground(DARKER_BG);
        JTable activeTable = createFlightTable(activeFlights);
        activePanel.add(new JScrollPane(activeTable), BorderLayout.CENTER);

        // Deleted flights panel
        JPanel deletedPanel = new JPanel(new BorderLayout());
        TitledBorder deletedBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(TEXT_COLOR),
                "Deleted Flights");
        deletedBorder.setTitleColor(TEXT_COLOR);
        deletedBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        deletedPanel.setBorder(BorderFactory.createCompoundBorder(
                deletedBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        deletedPanel.setBackground(DARKER_BG);
        JTable deletedTable = createFlightTable(deletedFlights);
        deletedPanel.add(new JScrollPane(deletedTable), BorderLayout.CENTER);

        // Add tables to split pane
        splitPane.setTopComponent(activePanel);
        splitPane.setBottomComponent(deletedPanel);

        // Add split pane to main panel
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add statistics panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setBackground(DARKER_BG);
        JLabel activeLabel = new JLabel("Active Flights: " + activeFlights.size());
        JLabel deletedLabel = new JLabel("Deleted Flights: " + deletedFlights.size());
        JLabel totalLabel = new JLabel("Total Flights: " + allFlights.size());

        // Style the labels
        Font statsFont = new Font("Segoe UI", Font.BOLD, 12);
        activeLabel.setFont(statsFont);
        deletedLabel.setFont(statsFont);
        totalLabel.setFont(statsFont);

        activeLabel.setForeground(SUCCESS_COLOR);
        deletedLabel.setForeground(ERROR_COLOR);
        totalLabel.setForeground(TEXT_COLOR);

        statsPanel.add(activeLabel);
        statsPanel.add(deletedLabel);
        statsPanel.add(totalLabel);

        mainPanel.add(statsPanel, BorderLayout.SOUTH);

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
        String[] columns = { "Booking ID", "Customer", "Flight", "Booking Date", "Final Price", "Payment Status" };
        Object[][] data = new Object[bookingsList.size()][6];
        for (int i = 0; i < bookingsList.size(); i++) {
            Booking booking = bookingsList.get(i);
            data[i][0] = booking.getId();
            data[i][1] = booking.getCustomer().getName();
            data[i][2] = booking.getFlight().getFlightNumber();
            data[i][3] = booking.getBookingDate();
            data[i][4] = String.format("$%.2f", booking.getFinalPrice()); // Show final price
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

        // Create main panel with dark theme gradient
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, DARK_BG, w, h, DARKER_BG);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setBorder(null);
        splitPane.setBackground(DARKER_BG);

        // Active customers panel
        JPanel activePanel = createCustomerPanel("Active Customers", activeCustomers,
                DARK_BG, DARKER_BG);

        // Deleted customers panel
        JPanel deletedPanel = createCustomerPanel("Deleted Customers", deletedCustomers,
                DARK_BG, DARKER_BG);

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

        // Create titled border with dark theme
        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                title);
        titledBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 14));
        titledBorder.setTitleColor(TEXT_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        // Create and style table
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
        };

        // Style the table
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(DARK_BG);
        table.setRowHeight(30);
        table.getTableHeader().setBackground(DARKER_BG);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

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

        // Add table to scroll pane with dark theme
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(DARKER_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);
        scrollPane.setBorder(BorderFactory.createLineBorder(ACCENT_COLOR, 1));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsPanel(int activeCount, int deletedCount) {
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        statsPanel.setBackground(DARKER_BG);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        Font statsFont = new Font("Segoe UI", Font.BOLD, 12);

        // Create statistics labels with dark theme
        JLabel activeLabel = new JLabel(String.format("Active Customers: %d", activeCount));
        JLabel deletedLabel = new JLabel(String.format("Deleted Customers: %d", deletedCount));
        JLabel totalLabel = new JLabel(String.format("Total Customers: %d", activeCount + deletedCount));

        // Style labels
        activeLabel.setFont(statsFont);
        deletedLabel.setFont(statsFont);
        totalLabel.setFont(statsFont);

        activeLabel.setForeground(SUCCESS_COLOR);
        deletedLabel.setForeground(ERROR_COLOR);
        totalLabel.setForeground(TEXT_COLOR);

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
            // Hide admin menu
            adminMenu.setVisible(false);

            // Disable administrative flight actions
            flightsAdd.setEnabled(false);
            flightsDel.setEnabled(false);

            // Modify bookings menu for customer
            bookingsMenu.removeAll();
            bookingsView = new JMenuItem("View Bookings");
            bookingsIssue = new JMenuItem("New Booking");
            bookingsCancel = new JMenuItem("Cancel Booking");
            bookingsPayment = new JMenuItem("Make Payment");

            bookingsView.addActionListener(e -> displayCustomerBookings(loggedInCustomerId));
            bookingsIssue.addActionListener(e -> new AddBookingWindow(this));
            bookingsCancel.addActionListener(e -> new CancelBookingWindow(this, loggedInCustomerId));
            bookingsPayment.addActionListener(e -> displayCustomerPayments(loggedInCustomerId));

            bookingsMenu.add(bookingsView);
            bookingsMenu.add(bookingsIssue);
            bookingsMenu.add(bookingsCancel);
            bookingsMenu.add(bookingsPayment);

            // Replace Customers menu with My Flights
            customersMenu.setText("My Flights");
            customersMenu.removeAll();
            JMenuItem viewMyFlights = new JMenuItem("View My Flights");
            viewMyFlights.addActionListener(e -> displayCustomerFlights(loggedInCustomerId));
            customersMenu.add(viewMyFlights);
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

    // Add new method to display customer flights in the main window
    private void displayCustomerFlights(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Flight> customerFlights = customer.getBookings().stream()
                    .map(Booking::getFlight)
                    .collect(Collectors.toList());

            // Create table model
            String[] columns = { "Flight Number", "From", "To", "Date", "Status", "Price" };
            Object[][] data = new Object[customerFlights.size()][6];

            for (int i = 0; i < customerFlights.size(); i++) {
                Flight flight = customerFlights.get(i);
                String status = flight.getDepartureDate().isAfter(LocalDate.now()) ? "Upcoming" : "Past";

                data[i] = new Object[] {
                        flight.getFlightNumber(),
                        flight.getOrigin(),
                        flight.getDestination(),
                        flight.getDepartureDate(),
                        status,
                        String.format("$%.2f", flight.calculatePrice(LocalDate.now()))
                };
            }

            // Create and configure table
            JTable table = new JTable(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            // Style the table
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

            // Use the custom renderer for the status column
            table.getColumnModel().getColumn(4).setCellRenderer(new StatusCellRenderer());

            // Create panel with summary
            JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
            mainPanel.add(new JScrollPane(table), BorderLayout.CENTER);

            // Add summary panel
            JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            summaryPanel.setBackground(new Color(245, 245, 245));
            summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            int upcomingCount = (int) customerFlights.stream()
                    .filter(f -> f.getDepartureDate().isAfter(LocalDate.now()))
                    .count();

            summaryPanel.add(new JLabel(String.format("Total Flights: %d", customerFlights.size())));
            summaryPanel.add(new JLabel(String.format("Upcoming Flights: %d", upcomingCount)));

            mainPanel.add(summaryPanel, BorderLayout.SOUTH);

            // Refresh main window
            getContentPane().removeAll();
            getContentPane().add(mainPanel);
            setTitle("Flight Booking System - My Flights");
            revalidate();
            repaint();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error loading flights: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add new method to display only customer's bookings
    private void displayCustomerBookings(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Booking> bookings = customer.getBookings();
            String[] columns = { "Booking ID", "Flight", "Booking Date", "Fee", "Payment Status" };
            Object[][] data = new Object[bookings.size()][5];

            for (int i = 0; i < bookings.size(); i++) {
                Booking booking = bookings.get(i);
                data[i][0] = booking.getId();
                data[i][1] = booking.getFlight().getFlightNumber();
                data[i][2] = booking.getBookingDate();
                data[i][3] = String.format("$%.2f", booking.getBookingFee());
                data[i][4] = booking.isPaymentProcessed() ? "Paid" : "Pending";
            }

            JTable table = new JTable(data, columns) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            refreshTable(table, "My Bookings");
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Add new method to display customer's payments
    private void displayCustomerPayments(int customerId) {
        try {
            Customer customer = fbs.getCustomerByID(customerId);
            List<Booking> unpaidBookings = customer.getBookings().stream()
                    .filter(b -> !b.isPaymentProcessed())
                    .collect(Collectors.toList());

            if (unpaidBookings.isEmpty()) {
                JOptionPane.showMessageDialog(this, "You have no pending payments.", "Payments",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Object[] options = unpaidBookings.stream()
                    .map(b -> String.format("Booking #%d - Flight %s ($%.2f)",
                            b.getId(),
                            b.getFlight().getFlightNumber(),
                            b.getBookingFee()))
                    .toArray();

            String selected = (String) JOptionPane.showInputDialog(this,
                    "Select a booking to pay:",
                    "Process Payment",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (selected != null) {
                int bookingId = Integer.parseInt(selected.split("#")[1].split(" ")[0]);
                Booking booking = fbs.getBookingByID(bookingId);
                new PaymentWindow(this, booking);
            }
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
