    static void drawStringInRect(JComponent c, Graphics g, String aString, int x, int y, int width, int height, int justification) {
        FontMetrics fontMetrics;
        int drawWidth, startX, startY, delta;
        if (g.getFont() == null) {
            return;
        }
        fontMetrics = SwingUtilities2.getFontMetrics(c, g);
        if (fontMetrics == null) {
            return;
        }
        if (justification == CENTER) {
            drawWidth = SwingUtilities2.stringWidth(c, fontMetrics, aString);
            if (drawWidth > width) {
                drawWidth = width;
            }
            startX = x + (width - drawWidth) / 2;
        } else if (justification == RIGHT) {
            drawWidth = SwingUtilities2.stringWidth(c, fontMetrics, aString);
            if (drawWidth > width) {
                drawWidth = width;
            }
            startX = x + width - drawWidth;
        } else {
            startX = x;
        }
        delta = (height - fontMetrics.getAscent() - fontMetrics.getDescent()) / 2;
        if (delta < 0) {
            delta = 0;
        }
        startY = y + height - delta - fontMetrics.getDescent();
        SwingUtilities2.drawString(c, g, aString, startX, startY);
    }
