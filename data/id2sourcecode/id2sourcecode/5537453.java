    public void draw(Graphics g) {
        if (ic == null) return;
        Color color = strokeColor != null ? strokeColor : ROIColor;
        g.setColor(color);
        x1d = x + x1R;
        y1d = y + y1R;
        x2d = x + x2R;
        y2d = y + y2R;
        x1 = (int) x1d;
        y1 = (int) y1d;
        x2 = (int) x2d;
        y2 = (int) y2d;
        int sx1 = ic.screenXD(x1d);
        int sy1 = ic.screenYD(y1d);
        int sx2 = ic.screenXD(x2d);
        int sy2 = ic.screenYD(y2d);
        int sx3 = sx1 + (sx2 - sx1) / 2;
        int sy3 = sy1 + (sy2 - sy1) / 2;
        Graphics2D g2d = (Graphics2D) g;
        if (stroke != null) g2d.setStroke(getScaledStroke());
        g.drawLine(sx1, sy1, sx2, sy2);
        if (wideLine && !overlay) {
            g2d.setStroke(onePixelWide);
            g.setColor(getColor());
            g.drawLine(sx1, sy1, sx2, sy2);
        }
        if (state != CONSTRUCTING && !overlay) {
            int size2 = HANDLE_SIZE / 2;
            handleColor = strokeColor != null ? strokeColor : ROIColor;
            drawHandle(g, sx1 - size2, sy1 - size2);
            handleColor = Color.white;
            drawHandle(g, sx2 - size2, sy2 - size2);
            drawHandle(g, sx3 - size2, sy3 - size2);
        }
        if (state != NORMAL) IJ.showStatus(imp.getLocationAsString(x2, y2) + ", angle=" + IJ.d2s(getAngle(x1, y1, x2, y2)) + ", length=" + IJ.d2s(getLength()));
        if (updateFullWindow) {
            updateFullWindow = false;
            imp.draw();
        }
    }
