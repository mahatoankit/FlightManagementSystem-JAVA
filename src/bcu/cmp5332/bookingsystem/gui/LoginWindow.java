package bcu.cmp5332.bookingsystem.gui;

import bcu.cmp5332.bookingsystem.model.Customer;
import bcu.cmp5332.bookingsystem.model.FlightBookingSystem;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class LoginWindow extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private final FlightBookingSystem fbs;
    private JTextField userIdField;
    private JPasswordField passwordField;

    // Custom colors for dark theme
    private static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color FOREGROUND_COLOR = new Color(255, 255, 255);
    private static final Color BUTTON_COLOR = new Color(75, 110, 175);
    private static final Color FIELD_BACKGROUND = new Color(60, 63, 65);
    private static final Color BORDER_COLOR = new Color(100, 100, 100);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);

    public LoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    private void initialize() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            
            // Set global UI properties
            UIManager.put("Panel.background", BACKGROUND_COLOR);
            UIManager.put("TextField.background", FIELD_BACKGROUND);
            UIManager.put("PasswordField.background", FIELD_BACKGROUND);
            UIManager.put("TextField.foreground", FOREGROUND_COLOR);
            UIManager.put("PasswordField.foreground", FOREGROUND_COLOR);
            UIManager.put("Label.foreground", FOREGROUND_COLOR);
            UIManager.put("Button.font", MAIN_FONT);
            UIManager.put("Label.font", MAIN_FONT);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        setTitle("Flight Booking System - Login");
        setSize(500, 400);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Flight Booking System", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(FOREGROUND_COLOR);
        titlePanel.add(titleLabel);

        // Main Panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // User ID field with styling
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel userLabel = createStyledLabel("User ID:");
        mainPanel.add(userLabel, gbc);

        gbc.gridx = 1;
        userIdField = createStyledTextField();
        mainPanel.add(userIdField, gbc);

        // Password field with styling
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel passLabel = createStyledLabel("Password:");
        mainPanel.add(passLabel, gbc);

        gbc.gridx = 1;
        passwordField = createStyledPasswordField();
        mainPanel.add(passwordField, gbc);

        // Buttons Panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonsPanel.setBackground(BACKGROUND_COLOR);
        
        JButton loginButton = createStyledButton("Login");
        JButton adminButton = createStyledButton("Admin Login");
        
        loginButton.addActionListener(this);
        adminButton.addActionListener(e -> adminLogin());

        buttonsPanel.add(loginButton);
        buttonsPanel.add(adminButton);

        // Add all panels to frame
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        // Center on screen
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(FOREGROUND_COLOR);
        label.setFont(MAIN_FONT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField(15);
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(FOREGROUND_COLOR);
        field.setFont(MAIN_FONT);
        field.setCaretColor(FOREGROUND_COLOR);
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setBackground(FIELD_BACKGROUND);
        field.setForeground(FOREGROUND_COLOR);
        field.setFont(MAIN_FONT);
        field.setCaretColor(FOREGROUND_COLOR);
        Border lineBorder = BorderFactory.createLineBorder(BORDER_COLOR);
        Border paddingBorder = BorderFactory.createEmptyBorder(5, 8, 5, 8);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));
        return field;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(FOREGROUND_COLOR);
        button.setFont(MAIN_FONT);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(BUTTON_COLOR.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(BUTTON_COLOR);
            }
        });
        
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            int userId = Integer.parseInt(userIdField.getText().trim());
            String password = new String(passwordField.getPassword());

            try {
                Customer customer = fbs.getCustomerByID(userId);
                MainWindow mainWindow = new MainWindow(fbs);
                mainWindow.setAdminMode(false);
                mainWindow.setLoggedInCustomerId(userId);
                dispose();
            } catch (Exception ex) {
                showErrorMessage("Invalid credentials", "Login Error");
            }
        } catch (NumberFormatException ex) {
            showErrorMessage("Please enter a valid user ID", "Input Error");
        }
    }

    /**
     * Prompts the user to enter the admin password. If the password is correct,
     * the admin mode is enabled in the main window and the login window is
     * disposed. If the password is incorrect, an error message is displayed.
     */
/**
 * Creates and shows a styled admin login dialog with password field.
 * Matches the dark theme of the main window.
 */
private void adminLogin() {
    // Create custom dialog
    JDialog dialog = new JDialog(this, "Admin Login", true);
    dialog.setLayout(new BorderLayout(10, 10));
    dialog.getContentPane().setBackground(BACKGROUND_COLOR);
    
    // Create password panel
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBackground(BACKGROUND_COLOR);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Add password label and field
    JLabel passLabel = createStyledLabel("Admin Password:");
    JPasswordField passField = createStyledPasswordField();
    
    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(passLabel, gbc);
    
    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(passField, gbc);
    
    // Create buttons panel
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
    buttonPanel.setBackground(BACKGROUND_COLOR);
    buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
    
    JButton loginBtn = createStyledButton("Login");
    JButton cancelBtn = createStyledButton("Cancel");
    
    // Add action listeners
    loginBtn.addActionListener(e -> {
        String password = new String(passField.getPassword());
        if (password.equals("admin")) {
            MainWindow mainWindow = new MainWindow(fbs);
            mainWindow.setAdminMode(true);
            dispose();
            dialog.dispose();
        } else {
            showErrorMessage("Invalid admin password", "Login Error");
        }
    });
    
    cancelBtn.addActionListener(e -> dialog.dispose());
    
    // Add buttons to panel
    buttonPanel.add(loginBtn);
    buttonPanel.add(cancelBtn);
    
    // Add components to dialog
    dialog.add(panel, BorderLayout.CENTER);
    dialog.add(buttonPanel, BorderLayout.SOUTH);
    
    // Configure dialog properties
    dialog.setSize(350, 200);
    dialog.setLocationRelativeTo(this);
    dialog.setResizable(false);
    
    // Handle ENTER key in password field
    passField.addActionListener(e -> loginBtn.doClick());
    
    dialog.setVisible(true);
}
    /**
     * Displays an error message dialog with the given message and title.
     * @param message the error message to display
     * @param title the title of the dialog
     */
    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
}