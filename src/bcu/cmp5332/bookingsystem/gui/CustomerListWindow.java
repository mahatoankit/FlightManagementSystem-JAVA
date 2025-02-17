package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * A GUI window that displays a list of all customers in the flight booking system.
 * This class provides a tabular view of customer information including their ID,
 * name, phone number, and active status.
 */
public class CustomerListWindow extends JFrame {

    /**
     * Constructs a new CustomerListWindow and initializes the GUI components.
     * 
     * @param fbs The FlightBookingSystem instance containing customer data to be displayed
     */
    public CustomerListWindow(FlightBookingSystem fbs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Set window title
        setTitle("Customer List");

        // Get customer data and prepare table contents
        List<Customer> customers = fbs.getCustomers();
        String[] columns = {"ID", "Name", "Phone", "Active"};
        Object[][] data = new Object[customers.size()][4];
        
        // Populate table data from customer list
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.isDeleted() ? "No" : "Yes";
        }

        // Create and configure the table
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);

        // Add table to scroll pane with padding
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Add scroll pane to window
        add(scrollPane, BorderLayout.CENTER);

        // Configure window properties
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}