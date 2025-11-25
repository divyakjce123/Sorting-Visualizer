package ui;

import java.awt.*;
import java.util.Arrays;
import javax.swing.*;

public class VisualizerPanel extends JPanel {
    private int[] array = new int[0];
    private int highlightA = -1, highlightB = -1;
    private boolean[] finalMarked = new boolean[0];
    
    // New: For the sweep animation
    private int sweepIndex = -1; 

    public VisualizerPanel() { setBackground(new Color(30, 30, 36)); }

    public void setArray(int[] arr) {
        array = arr == null ? new int[0] : arr.clone();
        finalMarked = new boolean[array.length];
        highlightA = highlightB = sweepIndex = -1;
        repaint();
    }

    public void setHighlight(int a, int b) { this.highlightA = a; this.highlightB = b; repaint(); }
    public void markFinal(int idx) { if (idx >= 0 && idx < finalMarked.length) finalMarked[idx] = true; repaint(); }
    public void clearFinalMarks() { finalMarked = new boolean[array.length]; repaint(); }
    
    // New: Setter for sweep animation
    public void setSweepIndex(int idx) { this.sweepIndex = idx; repaint(); }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth(), h = getHeight();
        int n = array.length;
        int barWidth = Math.max(6, (w - 120) / Math.max(1, n));
        int max = Arrays.stream(array).max().orElse(1);
        
        g2.setColor(new Color(40, 40, 48));
        for (int y = h - 80; y > 0; y -= 40) g2.fillRect(0, y - 1, w, 1);

        for (int i = 0; i < n; i++) {
            int x = 60 + i * barWidth;
            int barH = (int) ((array[i] / (double) max) * (h - 200));
            int y = h - barH - 100;
            
            Color fill;
            // Logic: Sweep -> Highlight -> Final -> Default
            if (sweepIndex >= 0 && i <= sweepIndex) {
                fill = new Color(50, 205, 50); // Lime Green for Success
            } else if (i == highlightA || i == highlightB) {
                fill = new Color(255, 99, 71); // Tomato Red for Active
            } else if (i < finalMarked.length && finalMarked[i]) {
                fill = new Color(98, 165, 255); // Blue for Sorted Part
            } else {
                fill = new Color(72, 160, 79); // Standard Green
            }

            g2.setColor(fill);
            g2.fillRoundRect(x, y, barWidth - 6, barH, 8, 8);
            g2.setColor(new Color(18, 18, 20));
            g2.drawRoundRect(x, y, barWidth - 6, barH, 8, 8);
            
            // Only draw text if bars are wide enough
            if (barWidth > 20) {
                g2.setColor(Color.WHITE);
                String v = String.valueOf(array[i]);
                int strw = g2.getFontMetrics().stringWidth(v);
                g2.drawString(v, x + Math.max(2, (barWidth - 6 - strw) / 2), y - 8);
            }
        }
    }
}