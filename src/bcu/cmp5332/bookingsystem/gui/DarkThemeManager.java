// package bcu.cmp5332.bookingsystem.gui;

// import javax.swing.*;
// import javax.swing.border.Border;
// import java.awt.*;

// public class DarkThemeManager {
//     // Theme Colors
//     public static final Color BACKGROUND = new Color(43, 43, 43);
//     public static final Color FOREGROUND = new Color(255, 255, 255);
//     public static final Color PRIMARY = new Color(75, 110, 175);
//     public static final Color SECONDARY = new Color(60, 63, 65);
//     public static final Color BORDER = new Color(100, 100, 100);

//     // Fonts
//     public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 24);
//     public static final Font MAIN_FONT = new Font("Segoe UI", Font.PLAIN, 14);
//     public static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

//     public static void setupTheme() {
//         UIManager.put("Panel.background", BACKGROUND);
//         UIManager.put("Panel.foreground", FOREGROUND);
//         UIManager.put("Label.foreground", FOREGROUND);
//         UIManager.put("TextField.background", SECONDARY);
//         UIManager.put("TextField.foreground", FOREGROUND);
//         UIManager.put("TextField.caretForeground", FOREGROUND);
//         UIManager.put("Button.background", PRIMARY);
//         UIManager.put("Button.foreground", FOREGROUND);
//         UIManager.put("OptionPane.background", BACKGROUND);
//         UIManager.put("OptionPane.messageForeground", FOREGROUND);
//     }

//     public static JButton styleButton(JButton button) {
//         button.setFont(BUTTON_FONT);
//         button.setBackground(PRIMARY);
//         button.setForeground(FOREGROUND);
//         button.setFocusPainted(false);
//         button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
//         return button;
//     }

//     public static JTextField styleTextField(JTextField field) {
//         field.setBackground(SECONDARY);
//         field.setForeground(FOREGROUND);
//         field.setCaretColor(FOREGROUND);
//         field.setFont(MAIN_FONT);
//         field.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(BORDER),
//                 BorderFactory.createEmptyBorder(5, 5, 5, 5)));
//         return field;
//     }

//     public static JPasswordField stylePasswordField(JPasswordField field) {
//         field.setBackground(SECONDARY);
//         field.setForeground(FOREGROUND);
//         field.setCaretColor(FOREGROUND);
//         field.setFont(MAIN_FONT);
//         field.setBorder(BorderFactory.createCompoundBorder(
//                 BorderFactory.createLineBorder(BORDER),
//                 BorderFactory.createEmptyBorder(5, 5, 5, 5)));
//         return field;
//     }

//     public static JPanel stylePanel(JPanel panel) {
//         panel.setBackground(BACKGROUND);
//         panel.setForeground(FOREGROUND);
//         return panel;
//     }

//     public static JLabel styleLabel(JLabel label) {
//         label.setForeground(FOREGROUND);
//         label.setFont(MAIN_FONT);
//         return label;
//     }

//     public static Border createTitledBorder(String title) {
//         return BorderFactory.createTitledBorder(
//                 BorderFactory.createLineBorder(BORDER),
//                 title,
//                 javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
//                 javax.swing.border.TitledBorder.DEFAULT_POSITION,
//                 TITLE_FONT,
//                 FOREGROUND);
//     }

//     public static void styleOptionPane(JOptionPane pane) {
//         pane.setBackground(BACKGROUND);
//         pane.setForeground(FOREGROUND);
//         pane.setFont(MAIN_FONT);

//         // Style the message area
//         Component[] components = pane.getComponents();
//         for (Component comp : components) {
//             if (comp instanceof JPanel) {
//                 stylePanel((JPanel) comp);
//             }
//         }
//     }
// }
