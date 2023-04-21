    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        if (d.width != maxWidth || d.height != textHeight) recalculateDimension();
        Insets insets = this.getInsets();
        int y = 0;
        if (verticalAlignment == TOP) {
            y = insets.top + lineAscent;
        } else if (verticalAlignment == CENTER) {
            y = insets.top + lineAscent;
            int clientAreaHeight = d.height - insets.top - insets.bottom;
            y = y + (clientAreaHeight - textHeight) / 2;
        } else if (verticalAlignment == BOTTOM) {
            int clientAreaBottom = d.height - insets.bottom;
            y = clientAreaBottom - textHeight;
            y += lineAscent;
        }
        for (int i = 0; i < numLines; i++) {
            int ha = getBidiHorizontalAlignment(horizontalAlignment);
            int x = 0;
            if (ha == LEFT) {
                ha = getBidiHorizontalAlignment(textAlignment);
                if (ha == LEFT) x = insets.left; else if (ha == RIGHT) x = maxWidth - lineWidths[i] + insets.left; else if (ha == CENTER) x = insets.left + (maxWidth - lineWidths[i]) / 2;
            } else if (ha == RIGHT) {
                ha = getBidiHorizontalAlignment(textAlignment);
                if (ha == LEFT) x = d.width - maxWidth - insets.right; else if (ha == RIGHT) x = d.width - lineWidths[i] - insets.right; else if (ha == CENTER) x = d.width - maxWidth - insets.right + (maxWidth - lineWidths[i]) / 2;
            } else if (ha == CENTER) {
                ha = getBidiHorizontalAlignment(textAlignment);
                int clientAreaWidth = d.width - insets.left - insets.right;
                if (ha == LEFT) x = insets.left + (clientAreaWidth - maxWidth) / 2; else if (ha == RIGHT) x = insets.left + (clientAreaWidth - maxWidth) / 2 + (maxWidth - lineWidths[i]); else if (ha == CENTER) x = insets.left + (clientAreaWidth - lineWidths[i]) / 2;
            }
            x += btnMarginWidth;
            g.drawString(lines[i], x, y);
            y += lineHeight;
        }
    }
