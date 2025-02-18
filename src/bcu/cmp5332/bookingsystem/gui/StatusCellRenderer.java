package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * Custom cell renderer for displaying flight status in tables with enhanced visual styling.
 * Provides distinct color coding for upcoming and past flights.
 */
public class StatusCellRenderer extends DefaultTableCellRenderer {
    
    // Define colors for different status types
    private static final Color UPCOMING_COLOR = new Color(75, 175, 80);  // Brighter green
    private static final Color PAST_COLOR = new Color(158, 158, 158);    // Medium gray
    private static final Color BG_COLOR = new Color(43, 43, 43);         // Dark background
    private static final Color SELECTED_BG = new Color(75, 110, 175);    // Selection blue

    /**
     * Renders the cell component with appropriate styling based on flight status.
     * 
     * @param table      the JTable being rendered
     * @param value      the value to assign to the cell
     * @param isSelected indicates if cell is selected
     * @param hasFocus   indicates if cell has focus
     * @param row        the row index of the cell
     * @param column     the column index of the cell
     * @return the configured component for rendering
     */
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, 
            boolean isSelected, boolean hasFocus, int row, int column) {
            
        Component c = super.getTableCellRendererComponent(table, value, 
                isSelected, hasFocus, row, column);
        
        // Set background color
        c.setBackground(isSelected ? SELECTED_BG : BG_COLOR);
        
        // Set text color based on status
        if ("Upcoming".equals(value)) {
            c.setForeground(UPCOMING_COLOR);
        } else {
            c.setForeground(PAST_COLOR);
        }
        
        // Add subtle padding and make text bold
        ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        setFont(getFont().deriveFont(Font.BOLD));
        
        return c;
    }
}