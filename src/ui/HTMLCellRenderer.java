package ui;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class HTMLCellRenderer extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = new JLabel("<html><div style='padding:6px;'>" + String.valueOf(value) + "</div></html>");
        lab.setVerticalAlignment(SwingConstants.TOP);
        lab.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        return lab;
    }
}