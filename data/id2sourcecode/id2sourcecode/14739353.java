    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = getSize();
        if (d.width != max_width || d.height != text_height) recalculateDimension();
        Insets insets = this.getInsets();
        int y = 0;
        if (verticalAlignment == TOP) {
            y = insets.top + line_ascent;
        } else if (verticalAlignment == CENTER) {
            y = insets.top + line_ascent;
            int clientAreaHeight = d.height - insets.top - insets.bottom;
            y = y + (clientAreaHeight - text_height) / 2;
        } else if (verticalAlignment == BOTTOM) {
            int clientAreaBottom = d.height - insets.bottom;
            y = clientAreaBottom - text_height;
            y += line_ascent;
        }
        for (int i = 0; i < num_lines; i++) {
            int ha = getBidiHorizontalAlignment(horizontalAlignment);
            int x = 0;
            if (ha == LEFT) {
                ha = getBidiHorizontalAlignment(textAlignment);
                if (ha == LEFT) x = insets.left; else if (ha == RIGHT) x = max_width - line_widths[i] + insets.left; else if (ha == CENTER) x = insets.left + (max_width - line_widths[i]) / 2;
            } else if (ha == RIGHT) {
                ha = getBidiHorizontalAlignment(textAlignment);
                if (ha == LEFT) x = d.width - max_width - insets.right; else if (ha == RIGHT) x = d.width - line_widths[i] - insets.right; else if (ha == CENTER) x = d.width - max_width - insets.right + (max_width - line_widths[i]) / 2;
            } else if (ha == CENTER) {
                ha = getBidiHorizontalAlignment(textAlignment);
                int clientAreaWidth = d.width - insets.left - insets.right;
                if (ha == LEFT) x = insets.left + (clientAreaWidth - max_width) / 2; else if (ha == RIGHT) x = insets.left + (clientAreaWidth - max_width) / 2 + (max_width - line_widths[i]); else if (ha == CENTER) x = insets.left + (clientAreaWidth - line_widths[i]) / 2;
            }
            x += btnMarginWidth;
            g.drawString(lines[i], x, y);
            y += line_height;
        }
    }
