package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddFlight;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.*;

/**
 * A window interface for adding new flights to the booking system.
 * This class provides a graphical user interface with form fields for flight
 * details.
 */
public class AddFlightWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField flightNoText = new JTextField();
    private JTextField originText = new JTextField();
    private JTextField destinationText = new JTextField();
    private JTextField depDateText = new JTextField();
    private JTextField basePriceText = new JTextField(); // For base price
    private JTextField capacityText = new JTextField(); // For capacity

    private JButton addBtn = new JButton("Add");
    private JButton cancelBtn = new JButton("Cancel");

    /**
     * Constructs a new AddFlightWindow.
     * 
     * @param mw the parent MainWindow instance
     */
    public AddFlightWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    /**
     * Initializes the window components and sets up the layout.
     * Creates and arranges all the necessary input fields and buttons.
     */
    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
        }

        setTitle("Add a New Flight");
        setSize(400, 300);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        topPanel.setBackground(new Color(240, 240, 240));

        topPanel.add(createStyledLabel("Flight No:"));
        topPanel.add(flightNoText);
        topPanel.add(createStyledLabel("Origin:"));
        topPanel.add(originText);
        topPanel.add(createStyledLabel("Destination:"));
        topPanel.add(destinationText);
        topPanel.add(createStyledLabel("Departure Date (YYYY-MM-DD):"));
        topPanel.add(depDateText);
        topPanel.add(createStyledLabel("Base Price ($):"));
        topPanel.add(basePriceText);
        topPanel.add(createStyledLabel("Capacity:"));
        topPanel.add(capacityText);

        JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        bottomPanel.setBackground(new Color(240, 240, 240));

        bottomPanel.add(new JLabel("     "));
        bottomPanel.add(createStyledButton(addBtn));
        bottomPanel.add(createStyledButton(cancelBtn));

        addBtn.addActionListener(this);
        cancelBtn.addActionListener(this);

        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(bottomPanel, BorderLayout.SOUTH);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    /**
     * Creates a styled label with custom font settings.
     * 
     * @param text the text to display in the label
     * @return a styled JLabel instance
     */
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    /**
     * Creates a styled button with custom appearance settings.
     * 
     * @param button the JButton to style
     * @return the styled JButton instance
     */
    private JButton createStyledButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    /**
     * Handles button click events for the Add and Cancel buttons.
     * 
     * @param ae the ActionEvent triggered by button click
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == addBtn) {
            addFlight();
        } else if (ae.getSource() == cancelBtn) {
            this.dispose();
        }
    }

    /**
     * Processes the addition of a new flight using the input field values.
     * Validates input data and creates a new AddFlight command.
     * Displays error messages if validation fails or if there are system
     * exceptions.
     */
    private void addFlight() {
        try {
            String flightNumber = flightNoText.getText();
            String origin = originText.getText();
            String destination = destinationText.getText();
            LocalDate departureDate = LocalDate.parse(depDateText.getText());
            double basePrice = Double.parseDouble(basePriceText.getText());
            int capacity = Integer.parseInt(capacityText.getText());

            AddFlight addFlightCmd = new AddFlight(flightNumber, origin, destination, departureDate, basePrice,
                    capacity);
            addFlightCmd.execute(mw.getFlightBookingSystem());
            mw.displayUpcomingFlights();
            this.dispose();
        } catch (DateTimeParseException dtpe) {
            JOptionPane.showMessageDialog(this, "Date must be in YYYY-MM-DD format", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this, "Price and Capacity must be numeric", "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}