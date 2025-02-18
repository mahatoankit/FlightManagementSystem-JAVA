package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class LightThemeManager {
    // Theme Colors
    public static final Color BACKGROUND = new Color(248, 250, 252);
    public static final Color FOREGROUND = new Color(30, 41, 59);
    public static final Color PRIMARY = new Color(79, 70, 229); // Indigo
    public static final Color SECONDARY = new Color(255, 255, 255); // White
    public static final Color BORDER = new Color(226, 232, 240);
    public static final Color HOVER = new Color(99, 102, 241); // Lighter indigo

    // Fonts
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    public static void setupTheme() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("Panel.foreground", FOREGROUND);
        UIManager.put("Label.foreground", FOREGROUND);
        UIManager.put("TextField.background", SECONDARY);
        UIManager.put("TextField.foreground", FOREGROUND);
        UIManager.put("TextField.caretForeground", FOREGROUND);
        UIManager.put("Button.background", PRIMARY);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("OptionPane.background", BACKGROUND);
        UIManager.put("OptionPane.messageForeground", FOREGROUND);
    }

    public static JButton styleButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(HOVER);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY);
            }
        });

        return button;
    }

    public static JTextField styleTextField(JTextField field) {
        field.setBackground(SECONDARY);
        field.setForeground(FOREGROUND);
        field.setCaretColor(FOREGROUND);
        field.setFont(MAIN_FONT);

        // Create a sophisticated border with padding
        Border lineBorder = BorderFactory.createLineBorder(BORDER, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        return field;
    }

    public static JPasswordField stylePasswordField(JPasswordField field) {
        field.setBackground(SECONDARY);
        field.setForeground(FOREGROUND);
        field.setCaretColor(FOREGROUND);
        field.setFont(MAIN_FONT);

        Border lineBorder = BorderFactory.createLineBorder(BORDER, 1);
        Border paddingBorder = BorderFactory.createEmptyBorder(8, 12, 8, 12);
        field.setBorder(BorderFactory.createCompoundBorder(lineBorder, paddingBorder));

        return field;
    }

    public static JPanel stylePanel(JPanel panel) {
        panel.setBackground(BACKGROUND);
        panel.setForeground(FOREGROUND);
        return panel;
    }

    public static JLabel styleLabel(JLabel label) {
        label.setForeground(FOREGROUND);
        label.setFont(MAIN_FONT);
        return label;
    }

    public static Border createTitledBorder(String title) {
        return BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BORDER, 1),
                title,
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                TITLE_FONT,
                FOREGROUND);
    }

    public static void styleOptionPane(JOptionPane pane) {
        pane.setBackground(BACKGROUND);
        pane.setForeground(FOREGROUND);
        pane.setFont(MAIN_FONT);

        Component[] components = pane.getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                stylePanel((JPanel) comp);
            }
        }
    }
}
