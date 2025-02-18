package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class CustomerListWindow extends JFrame {

    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);
    private static final Color ERROR_COLOR = new Color(255, 87, 34);

    public CustomerListWindow(FlightBookingSystem fbs) {
        // Set window title and properties
        setTitle("Customer List");
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(DARK_BG);

        // Create header panel
        JPanel headerPanel = createHeaderPanel();

        // Get customer data and prepare table contents
        List<Customer> customers = fbs.getCustomers();
        String[] columns = {"ID", "Name", "Phone", "Status"};
        Object[][] data = new Object[customers.size()][4];
        
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            data[i][0] = c.getId();
            data[i][1] = c.getName();
            data[i][2] = c.getPhone();
            data[i][3] = c.isDeleted() ? "Inactive" : "Active";
        }

        // Create and style the table
        JTable table = createStyledTable(data, columns);
        
        // Create styled scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBackground(DARK_BG);
        scrollPane.getViewport().setBackground(DARKER_BG);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT_COLOR, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Add summary panel
        JPanel summaryPanel = createSummaryPanel(customers);

        // Layout components
        add(headerPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        // Configure window properties
        setSize(600, 400);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(TEXT_COLOR);
        panel.add(titleLabel);

        return panel;
    }

    private JTable createStyledTable(Object[][] data, String[] columns) {
        JTable table = new JTable(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Style table
        table.setBackground(DARKER_BG);
        table.setForeground(TEXT_COLOR);
        table.setGridColor(DARK_BG);
        table.setSelectionBackground(ACCENT_COLOR);
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(35);
        
        // Style header
        table.getTableHeader().setBackground(DARKER_BG);
        table.getTableHeader().setForeground(TEXT_COLOR);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Center align all columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Custom renderer for Status column
        table.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    String status = (String) value;
                    setForeground("Active".equals(status) ? SUCCESS_COLOR : ERROR_COLOR);
                } else {
                    setForeground(Color.WHITE);
                }
                
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        return table;
    }

    private JPanel createSummaryPanel(List<Customer> customers) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        long activeCount = customers.stream().filter(c -> !c.isDeleted()).count();
        long inactiveCount = customers.size() - activeCount;

        addSummaryLabel(panel, "Total Customers: " + customers.size(), TEXT_COLOR);
        addSummaryLabel(panel, "Active: " + activeCount, SUCCESS_COLOR);
        addSummaryLabel(panel, "Inactive: " + inactiveCount, ERROR_COLOR);

        return panel;
    }

    private void addSummaryLabel(JPanel panel, String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(color);
        panel.add(label);
    }
}