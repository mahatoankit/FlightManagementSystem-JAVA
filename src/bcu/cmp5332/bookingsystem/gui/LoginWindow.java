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
    private JTextField userIdField = new JTextField(15); // Initialize with column width
    private JPasswordField passwordField = new JPasswordField(15); // Initialize with column width

    // Update the color constants
    private static final Color BACKGROUND_COLOR = new Color(43, 43, 43);
    private static final Color DARKER_BG = new Color(60, 63, 65);
    private static final Color TEXT_COLOR = new Color(187, 187, 187);
    private static final Color ACCENT_COLOR = new Color(75, 110, 175);
    private static final Color INPUT_BG = new Color(69, 73, 74);
    private static final Color BORDER_COLOR = new Color(100, 100, 100);
    private static final Color GRADIENT_START = new Color(75, 110, 175, 40);
    private static final Color GRADIENT_END = new Color(255, 64, 129, 30);
    private static final Color ERROR_COLOR = new Color(255, 87, 34);
    // Update fonts
    private static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    private static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public LoginWindow(FlightBookingSystem fbs) {
        this.fbs = fbs;
        initialize();
    }

    // Update the initialize method with new panel styling
    private void initialize() {
        setTitle("Flight Booking System - Login");

        setSize(500, 400);
        setLayout(new BorderLayout(20, 20));
        getContentPane().setBackground(BACKGROUND_COLOR);
        getContentPane().setForeground(TEXT_COLOR); // Set the text color
        UIManager.put("TitledBorder.titleColor", TEXT_COLOR); // Change the color of the title

        // Title Panel with gradient
        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, GRADIENT_START, getWidth(), 0, GRADIENT_END);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        titlePanel.setBackground(DARKER_BG); // Set background color to dark

        JLabel titleLabel = new JLabel("AL-Qaida Airlines");
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TEXT_COLOR);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(titleLabel);


        // Main login panel
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(DARKER_BG);
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(30, 40, 30, 40)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Add form components
        addFormField(mainPanel, "User ID:", userIdField, 0, gbc);
        addFormField(mainPanel, "Password:", passwordField, 1, gbc);

        // Buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonsPanel.setBackground(DARKER_BG);
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JButton loginButton = createStyledButton("Login", ACCENT_COLOR);
        JButton adminButton = createStyledButton("Admin Login", ACCENT_COLOR.darker());

        loginButton.addActionListener(this);
        adminButton.addActionListener(e -> adminLogin());

        buttonsPanel.add(loginButton);
        buttonsPanel.add(adminButton);

        // Add panels to frame
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);
    }

    // Add new helper methods
    private void addFormField(JPanel panel, String labelText, JTextField field, int row, GridBagConstraints gbc) {
        JLabel label = new JLabel(labelText);
        label.setFont(MAIN_FONT);
        label.setForeground(TEXT_COLOR);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        panel.add(label, gbc);

        styleTextField(field);
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        panel.add(field, gbc);
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setBackground(INPUT_BG);
        field.setForeground(TEXT_COLOR);
        field.setCaretColor(TEXT_COLOR);
        field.setFont(MAIN_FONT);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)));
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(baseColor);
        button.setForeground(TEXT_COLOR);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));

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
        panel.setBackground(DARKER_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT_COLOR, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add password label and field
        JLabel passLabel = new JLabel("Admin Password:");
        passLabel.setFont(MAIN_FONT);
        passLabel.setForeground(TEXT_COLOR);

        JPasswordField passField = new JPasswordField(20);
        styleTextField(passField);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(passLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passField, gbc);

        // Create buttons panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setBackground(DARKER_BG);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Create styled buttons with appropriate colors
        JButton loginBtn = createStyledButton("Login", ACCENT_COLOR);
        JButton cancelBtn = createStyledButton("Cancel", ERROR_COLOR);

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
     * 
     * @param message the error message to display
     * @param title   the title of the dialog
     */
    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }
}