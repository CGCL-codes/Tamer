    public void paintComponent(Graphics2D g) {
        int x = padding;
        int y = padding;
        int iconWidth = getIconWidth();
        String drawString = this.string;
        if ("".equals(drawString)) {
            drawString = null;
        }
        int combinedwidth = getCombinedWidth();
        if (font != null && drawString != null && combinedwidth > width - (padding * 2)) {
            combinedwidth = width - (padding * 2);
            int w = ((((textPosition & Graphics.HCENTER) == 0) && iconWidth > 0) ? (combinedwidth - iconWidth - gap) : combinedwidth) - font.getWidth(extension);
            int a = TextArea.searchStringCharOffset(drawString, font, w);
            drawString = drawString.substring(0, a) + extension;
        }
        int combinedheight = getCombinedHeight();
        if ((alignment & Graphics.HCENTER) != 0) {
            x = (width - combinedwidth) / 2;
        } else if ((alignment & Graphics.RIGHT) != 0) {
            x = (width - combinedwidth) - padding;
        }
        if ((alignment & Graphics.VCENTER) != 0) {
            y = (height - combinedheight) / 2;
        } else if ((alignment & Graphics.BOTTOM) != 0) {
            y = (height - combinedheight) - padding;
        }
        if (iconWidth > 0) {
            int ix = x;
            int iy = y;
            if ((textPosition & Graphics.HCENTER) != 0) {
                ix = x + (combinedwidth - iconWidth) / 2;
            } else if ((textPosition & Graphics.LEFT) != 0 && font != null && drawString != null) {
                ix = x + font.getWidth(drawString) + gap;
            }
            if ((textPosition & Graphics.VCENTER) != 0 || (textPosition & Graphics.RIGHT) != 0 || (textPosition & Graphics.LEFT) != 0) {
                iy = y + (combinedheight - getIconHeight()) / 2;
            } else if ((textPosition & Graphics.TOP) != 0 && font != null && drawString != null) {
                iy = y + font.getHeight() + gap;
            }
            paintIcon(g, ix, iy);
        }
        if (font != null && drawString != null) {
            int tx = x;
            int ty = y;
            if ((textPosition & Graphics.HCENTER) != 0) {
                tx = x + (combinedwidth - font.getWidth(drawString)) / 2;
            } else if ((textPosition & Graphics.RIGHT) != 0 && iconWidth > 0) {
                tx = x + iconWidth + gap;
            }
            if ((textPosition & Graphics.VCENTER) != 0) {
                ty = y + (combinedheight - font.getHeight()) / 2;
            } else if ((textPosition & Graphics.BOTTOM) != 0 && iconWidth > 0) {
                ty = y + getIconHeight() + gap;
            }
            g.setColor(getForeground());
            g.setFont(font);
            g.drawString(drawString, tx, ty);
        }
    }
