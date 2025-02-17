package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.commands.AddCustomer;
import bcu.cmp5332.bookingsystem.main.FlightBookingSystemException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AddCustomerWindow extends JFrame implements ActionListener {

    private MainWindow mw;
    private JTextField nameField = new JTextField(20);
    private JTextField phoneField = new JTextField(15);
    private JTextField emailField = new JTextField(30);
    private JPasswordField passwordField = new JPasswordField(20);
    private JButton addBtn = new JButton("Add");

    public AddCustomerWindow(MainWindow mw) {
        this.mw = mw;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Add New Customer");
        setSize(400, 250);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(new Color(240, 240, 240));

        panel.add(createStyledLabel("Name:"));
        panel.add(nameField);
        panel.add(createStyledLabel("Phone:"));
        panel.add(phoneField);
        panel.add(createStyledLabel("Email:"));
        panel.add(emailField);
        panel.add(createStyledLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(createStyledButton(addBtn));

        addBtn.addActionListener(this);

        getContentPane().add(panel, BorderLayout.CENTER);
        setLocationRelativeTo(mw);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return label;
    }

    private JButton createStyledButton(JButton button) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(new Color(0, 123, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String name = nameField.getText();
            String phone = phoneField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            AddCustomer addCmd = new AddCustomer(name, phone, email, password);
            addCmd.execute(mw.getFlightBookingSystem());
            mw.displayAllCustomers();
            this.dispose();
        } catch (FlightBookingSystemException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}