package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import bcu.cmp5332.bookingsystem.data.FlightBookingSystemData;
import bcu.cmp5332.bookingsystem.model.Customer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AddCustomerWindow extends JFrame implements ActionListener {

    private final MainWindow mw;
    private final JTextField nameField = new JTextField();
    private final JTextField phoneField = new JTextField();
    private final JTextField emailField = new JTextField();
    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    // Add color constants
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);
    private static final Color ERROR_COLOR = new Color(255, 87, 34);

    public AddCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        setTitle("Add New Customer");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(mw);

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form panel with styled background
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(DARKER_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        // Add title label
        JLabel titleLabel = new JLabel("Add New Customer");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);

        // Style and add form fields
        // Set preferred size for text fields to make them wider
        nameField.setPreferredSize(new Dimension(200, 35));
        phoneField.setPreferredSize(new Dimension(200, 35));
        emailField.setPreferredSize(new Dimension(200, 35));

        addFormField(formPanel, "Name:", nameField, 0, gbc);
        addFormField(formPanel, "Phone:", phoneField, 1, gbc);
        addFormField(formPanel, "Email:", emailField, 2, gbc);

        // Button panel with dark background
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(DARKER_BG);

        // Style buttons
        addBtn = createStyledButton("Add", SUCCESS_COLOR);
        cancelBtn = createStyledButton("Cancel", ERROR_COLOR);

        buttonPanel.add(addBtn);
        buttonPanel.add(cancelBtn);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        // Label
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.0;
        panel.add(label, gbc);

        // Text field
        styleTextField(field);
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(baseColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(baseColor.brighter());
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });

        button.addActionListener(this);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        if (ev.getSource() == addBtn) {
            addCustomer();
        } else if (ev.getSource() == cancelBtn) {
            dispose();
        }
    }

    private void addCustomer() {
        try {
            // Input validation
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
                throw new FlightBookingSystemException("All fields are required.");
            }

            // Email validation
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new FlightBookingSystemException("Invalid email format.");
            }

            // Phone validation
            if (!phone.matches("^[0-9+()-]{10,15}$")) {
                throw new FlightBookingSystemException("Invalid phone number format.");
            }

            // Get the flight booking system and add the customer
            FlightBookingSystem fbs = mw.getFlightBookingSystem();
            fbs.addCustomer(name, phone, email);

            // After successful addition, show the generated password
            Customer lastAdded = fbs.getCustomers().stream()
                    .filter(c -> c.getEmail().equals(email))
                    .findFirst()
                    .orElseThrow(() -> new FlightBookingSystemException("Failed to retrieve added customer"));

            // Show success message with password
            JOptionPane.showMessageDialog(this,
                    "Customer added successfully!\nTemporary password: " + lastAdded.getPassword() +
                            "\nPlease make sure to save this password.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            // Save the updated system state
            FlightBookingSystemData.store(fbs);

            // Refresh the customers display in main window
            mw.displayAllCustomers();

            // Close the add customer window
            dispose();

        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "An unexpected error occurred: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}