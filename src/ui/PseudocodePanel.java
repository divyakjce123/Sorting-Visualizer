package ui;

import java.awt.*;
import javax.swing.*;

public class PseudocodePanel extends JPanel {
    private String[] lines = new String[0];
    private int highlight = -1;
    private final Font mono = new Font(Font.MONOSPACED, Font.PLAIN, 14);

    public PseudocodePanel() { setBackground(new Color(30, 30, 36)); }
    public void setLines(String[] l) { lines = l == null ? new String[0] : l; repaint(); }
    public void highlightLine(int idx) { highlight = idx; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(mono);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int y = 20;
        for (int i = 0; i < lines.length; i++) {
            if (i == highlight) {
                g2.setColor(new Color(60, 44, 26));
                g2.fillRect(0, y - 14, getWidth(), 22);
                g2.setColor(new Color(255, 205, 86));
            } else {
                g2.setColor(new Color(200, 200, 200));
            }
            g2.drawString((i + 1) + ". " + lines[i], 12, y);
            y += 22;
        }
    }
}