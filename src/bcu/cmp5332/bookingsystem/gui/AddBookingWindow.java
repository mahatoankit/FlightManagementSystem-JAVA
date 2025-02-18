package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddBooking;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import bcu.cmp5332.bookingsystem.model.Booking;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class AddBookingWindow extends JFrame implements ActionListener {
    // Add color constants
    private static final Color DARK_BG = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);
    private static final Color SUCCESS_COLOR = new Color(75, 175, 80);

    private MainWindow mw;
    private JTextField custIdField;
    private JTextField flightIdField;
    private JLabel feeLabel = new JLabel("Booking Fee: $0.00");
    private JButton computeFeeButton = new JButton("Compute Fee");
    private JButton bookButton = new JButton("Book Flight");

    private double computedFee = 0.0;

    public AddBookingWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {

        // Title label
        JLabel titleLabel = new JLabel("Add New Booking");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        custIdField = new JTextField();
        flightIdField = new JTextField();

        setTitle("Add sffsdNew Booking");
        setSize(500, 450); // increased from 450 to 500
        setLayout(new BorderLayout(10, 10));

        // Main panel with dark background
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(DARK_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(DARKER_BG);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        

        // Set preferred size for fields
        Dimension fieldSize = new Dimension(250, 35);
        custIdField.setPreferredSize(fieldSize);
        flightIdField.setPreferredSize(fieldSize);

        // Update GridBagConstraints for better field sizing
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.weightx = 1.0; // Give weight to allow horizontal expansion

        // Add components
        addFormField(formPanel, "Customer ID:", custIdField, 0, gbc);
        addFormField(formPanel, "Flight ID:", flightIdField, 1, gbc);

        // Style fee label
        feeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        feeLabel.setForeground(TEXT_COLOR);
        feeLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Style buttons
        computeFeeButton = createStyledButton("Compute Fee", ACCENT_COLOR);
        bookButton = createStyledButton("Book Flight", SUCCESS_COLOR);

        // Add components to form panel
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        formPanel.add(computeFeeButton, gbc);

        gbc.gridy = 3;
        formPanel.add(feeLabel, gbc);

        gbc.gridy = 4;
        formPanel.add(bookButton, gbc);

        // If customer is logged in
        if (mw.getLoggedInCustomerId() != null) {
            custIdField.setText(String.valueOf(mw.getLoggedInCustomerId()));
            custIdField.setEditable(false);
        }

        // Add action listeners
        computeFeeButton.addActionListener(this);
        bookButton.addActionListener(this);

        // Layout
        mainPanel.add(titleLabel, gbc);
        gbc.gridy = 1;
        mainPanel.add(formPanel, gbc);

        add(mainPanel);
        setLocationRelativeTo(mw);
        setResizable(false);
        setVisible(true);
    }

    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_COLOR);

        // Label constraints
        GridBagConstraints labelGbc = (GridBagConstraints) gbc.clone();
        labelGbc.gridx = 0;
        labelGbc.gridy = row;
        labelGbc.gridwidth = 1;
        labelGbc.weightx = 0.2; // Give less weight to label
        panel.add(label, labelGbc);

        // Field constraints
        GridBagConstraints fieldGbc = (GridBagConstraints) gbc.clone();
        fieldGbc.gridx = 1;
        fieldGbc.gridy = row;
        fieldGbc.gridwidth = 1;
        fieldGbc.weightx = 0.8; // Give more weight to field

        styleTextField(field);
        panel.add(field, fieldGbc);
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

        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == computeFeeButton) {
            computeFee();
        } else if (e.getSource() == bookButton) {
            createBooking();
        }
    }

    private void computeFee() {
        try {
            int flightId = Integer.parseInt(flightIdField.getText());
            double fee = mw.getFlightBookingSystem().getFlightByID(flightId).calculatePrice(LocalDate.now());
            computedFee = fee;
            feeLabel.setText(String.format("Booking Fee: $%.2f", fee));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Flight ID", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createBooking() {
        try {
            int custId = Integer.parseInt(custIdField.getText());
            int flightId = Integer.parseInt(flightIdField.getText());
            AddBooking addCmd = new AddBooking(custId, flightId, LocalDate.now());
            addCmd.execute(mw.getFlightBookingSystem());
            Booking latestBooking = mw.getFlightBookingSystem().getBookings()
                    .get(mw.getFlightBookingSystem().getBookings().size() - 1);
            JOptionPane.showMessageDialog(this,
                    "Booking created successfully.\nFee: $" + String.format("%.2f", computedFee));
            mw.displayBookings();
            this.dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid input. Please check IDs.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
