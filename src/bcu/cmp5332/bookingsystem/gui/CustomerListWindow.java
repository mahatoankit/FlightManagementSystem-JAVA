package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CustomerListWindow extends JFrame {

    public CustomerListWindow(FlightBookingSystem fbs) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Customer List");
        List<Customer> customers = fbs.getCustomers();
        String[] columns = {"ID", "Name", "Phone", "Active"};
        Object[][] data = new Object[customers.size()][4];
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.isDeleted() ? "No" : "Yes";
        }
        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}