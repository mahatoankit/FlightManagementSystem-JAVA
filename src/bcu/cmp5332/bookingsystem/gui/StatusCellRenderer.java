package bcu.cmp5332.bookingsystem.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    private static final Color UPCOMING_COLOR = new Color(46, 125, 50);
    private static final Color PAST_COLOR = new Color(156, 156, 156);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
            int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if ("Upcoming".equals(value)) {
            c.setForeground(UPCOMING_COLOR);
        } else {
            c.setForeground(PAST_COLOR);
        }
        return c;
    }
}
