    void paint(Graphics g, int x, int y, int width, int height, Component c) {
        int originalColor = g.getColor();
        if (!themeColors) {
            g.setColor(colorA);
        }
        switch(type) {
            case TYPE_LINE:
                if (borderTitle == null) {
                    width--;
                    height--;
                    for (int iter = 0; iter < thickness; iter++) {
                        g.drawRect(x + iter, y + iter, width, height);
                        width -= 2;
                        height -= 2;
                    }
                } else {
                    Font f = c.getStyle().getFont();
                    int titleW = f.stringWidth(borderTitle);
                    int topPad = c.getStyle().getPadding(Component.TOP);
                    int topY = y + (topPad - thickness) / 2;
                    if (c.isRTL()) {
                        g.fillRect(x + width - TITLE_MARGIN, topY, TITLE_MARGIN, thickness);
                        g.fillRect(x, topY, width - (TITLE_MARGIN + titleW + TITLE_SPACE * 2), thickness);
                        g.drawString(borderTitle, x + width - (TITLE_MARGIN + titleW + TITLE_SPACE), y + (topPad - f.getHeight()) / 2);
                    } else {
                        g.fillRect(x, topY, TITLE_MARGIN, thickness);
                        g.fillRect(x + TITLE_MARGIN + titleW + TITLE_SPACE * 2, topY, width - (TITLE_MARGIN + titleW + TITLE_SPACE * 2), thickness);
                        g.drawString(borderTitle, x + TITLE_MARGIN + TITLE_SPACE, y + (topPad - f.getHeight()) / 2);
                    }
                    g.fillRect(x, y + height - thickness, width, thickness);
                    g.fillRect(x, topY, thickness, height);
                    g.fillRect(x + width - thickness, topY, thickness, height);
                }
                break;
            case TYPE_DASHED:
            case TYPE_DOTTED:
                int segWidth = thickness;
                if (type == TYPE_DASHED) {
                    segWidth = thickness * 3;
                }
                int ix = x;
                for (; ix < x + width; ix += segWidth * 2) {
                    g.fillRect(ix, y, segWidth, thickness);
                    g.fillRect(ix, y + height - thickness, segWidth, thickness);
                }
                if (ix - segWidth < x + width) {
                    g.fillRect(ix - segWidth, y, x + width - ix + segWidth, thickness);
                    g.fillRect(ix - segWidth, y + height - thickness, x + width - ix + segWidth, thickness);
                }
                int iy = y;
                for (; iy < y + height; iy += segWidth * 2) {
                    g.fillRect(x, iy, thickness, segWidth);
                    g.fillRect(x + width - thickness, iy, thickness, segWidth);
                }
                if (iy - segWidth < y + height) {
                    g.fillRect(x, iy - segWidth, thickness, y + height - iy + segWidth);
                    g.fillRect(x + width - thickness, iy - segWidth, thickness, y + height - iy + segWidth);
                }
                break;
            case TYPE_DOUBLE:
                width--;
                height--;
                for (int iter = 0; iter < thickness; iter++) {
                    if ((iter * 100 / thickness <= 33) || (iter * 100 / thickness >= 66)) {
                        g.drawRect(x + iter, y + iter, width, height);
                    }
                    width -= 2;
                    height -= 2;
                }
                break;
            case TYPE_INSET:
            case TYPE_OUTSET:
                for (int i = 0; i < thickness; i++) {
                    g.drawLine(x + i, y + i, x + i, y + height - i);
                    g.drawLine(x + i, y + i, x + width - i, y + i);
                }
                if (type == TYPE_INSET) {
                    g.lighterColor(50);
                } else {
                    g.darkerColor(50);
                }
                for (int i = 0; i < thickness; i++) {
                    g.drawLine(x + i, y + height - i, x + width - i, y + height - i);
                    g.drawLine(x + width - i, y + i, x + width - i, y + height - i);
                }
                break;
            case TYPE_GROOVE:
            case TYPE_RIDGE:
                for (int i = 0; i < thickness / 2; i++) {
                    g.drawLine(x + i, y + i, x + i, y + height - i);
                    g.drawLine(x + i, y + i, x + width - i, y + i);
                }
                for (int i = thickness / 2; i < thickness; i++) {
                    g.drawLine(x + i, y + height - i, x + width - i, y + height - i);
                    g.drawLine(x + width - i, y + i, x + width - i, y + height - i);
                }
                if (type == TYPE_GROOVE) {
                    g.lighterColor(50);
                } else {
                    g.darkerColor(50);
                }
                for (int i = 0; i < thickness / 2; i++) {
                    g.drawLine(x + i, y + height - i, x + width - i, y + height - i);
                    g.drawLine(x + width - i, y + i, x + width - i, y + height - i);
                }
                for (int i = thickness / 2; i < thickness; i++) {
                    g.drawLine(x + i, y + i, x + i, y + height - i);
                    g.drawLine(x + i, y + i, x + width - i, y + i);
                }
                break;
            case TYPE_ROUNDED_PRESSED:
                x++;
                y++;
                width -= 2;
                height -= 2;
            case TYPE_ROUNDED:
                width--;
                height--;
                if (outline) {
                    g.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
                }
                break;
            case TYPE_ETCHED_LOWERED:
            case TYPE_ETCHED_RAISED:
                g.drawRect(x, y, width - 2, height - 2);
                if (themeColors) {
                    if (type == TYPE_ETCHED_LOWERED) {
                        g.lighterColor(40);
                    } else {
                        g.darkerColor(40);
                    }
                } else {
                    g.setColor(colorB);
                }
                g.drawLine(x + 1, y + height - 3, x + 1, y + 1);
                g.drawLine(x + 1, y + 1, x + width - 3, y + 1);
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y + height - 1, x + width - 1, y);
                break;
            case TYPE_BEVEL_RAISED:
                if (themeColors) {
                    g.setColor(getBackgroundColor(c));
                    g.lighterColor(50);
                } else {
                    g.setColor(colorA);
                }
                g.drawLine(x, y, x, y + height - 2);
                g.drawLine(x + 1, y, x + width - 2, y);
                if (themeColors) {
                    g.darkerColor(25);
                } else {
                    g.setColor(colorB);
                }
                g.drawLine(x + 1, y + 1, x + 1, y + height - 3);
                g.drawLine(x + 2, y + 1, x + width - 3, y + 1);
                if (themeColors) {
                    g.darkerColor(50);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y, x + width - 1, y + height - 2);
                if (themeColors) {
                    g.darkerColor(25);
                } else {
                    g.setColor(colorD);
                }
                g.drawLine(x + 1, y + height - 2, x + width - 2, y + height - 2);
                g.drawLine(x + width - 2, y + 1, x + width - 2, y + height - 3);
                break;
            case TYPE_BEVEL_LOWERED:
                if (themeColors) {
                    g.setColor(getBackgroundColor(c));
                    g.darkerColor(50);
                } else {
                    g.setColor(colorD);
                }
                g.drawLine(x, y, x, y + height - 1);
                g.drawLine(x + 1, y, x + width - 1, y);
                if (themeColors) {
                    g.lighterColor(25);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x + 1, y + 1, x + 1, y + height - 2);
                g.drawLine(x + 2, y + 1, x + width - 2, y + 1);
                if (themeColors) {
                    g.lighterColor(50);
                } else {
                    g.setColor(colorC);
                }
                g.drawLine(x + 1, y + height - 1, x + width - 1, y + height - 1);
                g.drawLine(x + width - 1, y + 1, x + width - 1, y + height - 2);
                if (themeColors) {
                    g.lighterColor(25);
                } else {
                    g.setColor(colorA);
                }
                g.drawLine(x + 2, y + height - 2, x + width - 2, y + height - 2);
                g.drawLine(x + width - 2, y + 2, x + width - 2, y + height - 3);
                break;
            case TYPE_COMPOUND:
                Style style = c.getStyle();
                boolean drawLeft = true;
                boolean drawRight = true;
                if (c.getUIManager().getLookAndFeel().isRTL()) {
                    boolean temp = drawLeft;
                    drawLeft = drawRight;
                    drawRight = temp;
                }
                Border top = compoundBorders[Component.TOP];
                Border bottom = compoundBorders[Component.BOTTOM];
                Border left = compoundBorders[Component.LEFT];
                Border right = compoundBorders[Component.RIGHT];
                int topThickness = 0;
                int bottomThickness = 0;
                if (top != null) {
                    Rectangle clip = saveClip(g);
                    topThickness = top.thickness;
                    g.clipRect(x, y, width, topThickness);
                    top.paint(g, x, y, width, height, c);
                    restoreClip(g, clip);
                }
                if (bottom != null) {
                    Rectangle clip = saveClip(g);
                    bottomThickness = bottom.thickness;
                    g.clipRect(x, y + height - bottomThickness, width, bottomThickness);
                    bottom.paint(g, x, y, width, height, c);
                    restoreClip(g, clip);
                }
                if ((drawLeft) && (left != null)) {
                    Rectangle clip = saveClip(g);
                    g.clipRect(x, y + topThickness, left.thickness, height - topThickness - bottomThickness);
                    left.paint(g, x, y, width, height, c);
                    restoreClip(g, clip);
                }
                if ((drawRight) && (right != null)) {
                    Rectangle clip = saveClip(g);
                    g.clipRect(x + width - right.thickness, y + topThickness, right.thickness, height - topThickness - bottomThickness);
                    right.paint(g, x, y, width, height, c);
                    restoreClip(g, clip);
                }
                break;
            case TYPE_IMAGE:
            case TYPE_IMAGE_SCALED:
            case TYPE_IMAGE_HORIZONTAL:
            case TYPE_IMAGE_VERTICAL:
                break;
        }
        g.setColor(originalColor);
    }
