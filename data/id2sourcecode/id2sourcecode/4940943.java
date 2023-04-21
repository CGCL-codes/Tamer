    @Override
    public void paintLegend(final GCWrapper gc) {
        final Rectangle clipping = gc.getClipping();
        final int left = clipping.x;
        final int top = clipping.y;
        final int right = clipping.x + clipping.width;
        final int bottom = clipping.y + clipping.width;
        final int midx = (left + right) / 2;
        final int midy = (top + bottom) / 2;
        drawStationline(gc, midx, midy, midx, bottom);
        gc.setLineWidth(1);
        gc.setLineStyle(SWT.LINE_SOLID);
        gc.setForeground(m_colors[0]);
        gc.drawOval(midx - 2, midy - 2, 4, 4);
        gc.drawLine(left, top, midx, midy);
        gc.drawLine(midx, midy, right, midy);
    }
