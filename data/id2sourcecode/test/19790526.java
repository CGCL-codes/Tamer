    private void paintGraph(Graphics g) {
        super.paintComponent(g);
        final Color oldColor = g.getColor();
        final Font oldFont = g.getFont();
        final Font tempFont = new Font(oldFont.getName(), Font.PLAIN, oldFont.getSize() - 2);
        g.setFont(tempFont);
        final FontMetrics m = g.getFontMetrics();
        final int rowHeight = m.getHeight() + 1;
        final int rowBase = m.getLeading() / 2 + m.getMaxAscent();
        final int yBoxColor = (rowHeight - COLOR_BOX_WIDTH) / 2;
        final int yOrig = 0;
        final int xName = getWidth() - RECT_WIDTH - 10;
        final int xCurrent = xName + NAME_COLUMN_WIDTH;
        final int xAverage = xCurrent + CURRENT_COLUMN_WIDTH;
        final int xMax = xAverage + AVERAGE_COLUMN_WIDTH;
        final int xTotal = xMax + MAX_COLUMN_WIDTH;
        final int xColor = xTotal + TOTAL_COLUMN_WIDTH;
        final int margin = 3;
        final int xBoxColor = xColor + (COLOR_COLUMN_WIDTH - COLOR_BOX_WIDTH) / 2;
        int y = yOrig;
        g.drawString(NAME, xName + margin, y + rowBase);
        g.drawString(CURRENT, xCurrent + margin, y + rowBase);
        g.drawString(AVERAGE, xAverage + margin, y + rowBase);
        g.drawString(MAX, xMax + margin, y + rowBase);
        g.drawString(TOTAL, xTotal + margin, y + rowBase);
        g.drawString(COLOR, xColor + margin, y + rowBase);
        y += rowHeight;
        g.drawLine(xName, y, xName + RECT_WIDTH, y);
        y++;
        final Iterator iter = LIST.iterator();
        while (iter.hasNext()) {
            final StatHandler handler = (StatHandler) iter.next();
            final Statistic stat = handler.getStat();
            final int[] Y_COORDS = handler.getData();
            final IntBuffer buf = stat.getStatHistory();
            g.setColor(handler.getColor());
            if (!(stat instanceof NumericalStatistic)) {
                synchronized (buf) {
                    for (int j = 0; j < buf.size(); j++) {
                        int yVal = (int) (buf.get(j) * _yPixelFactor / AXIS_DATA.getYScale());
                        Y_COORDS[j] = getHeight() - GraphBorder.getOffset() - yVal;
                    }
                }
                g.drawPolyline(X_COORDS, Y_COORDS, Y_COORDS.length);
            }
            String current;
            synchronized (buf) {
                current = NUMBER_FORMAT.format((double) buf.get(Y_COORDS.length - 1) / AXIS_DATA.getYScale());
            }
            g.fillRect(xBoxColor, y + yBoxColor, COLOR_BOX_WIDTH, COLOR_BOX_WIDTH);
            g.setColor(oldColor);
            g.drawRect(xBoxColor - 1, y + yBoxColor - 1, COLOR_BOX_WIDTH + 1, COLOR_BOX_WIDTH + 1);
            final String name = handler.getDisplayName();
            NUMBER_FORMAT.setMaximumFractionDigits(3);
            final String average = NUMBER_FORMAT.format(stat.getAverage());
            final String max = NUMBER_FORMAT.format(stat.getMax());
            NUMBER_FORMAT.setMaximumFractionDigits(1);
            final String total = NUMBER_FORMAT.format(stat.getTotal());
            g.drawString(name, xName + margin, y + rowBase);
            g.drawString(current, xCurrent + margin, y + rowBase);
            g.drawString(average, xAverage + margin, y + rowBase);
            g.drawString(max, xMax + margin, y + rowBase);
            g.drawString(total, xTotal + margin, y + rowBase);
            y += rowHeight;
        }
        g.drawRect(xName, yOrig, RECT_WIDTH, y - yOrig);
        g.drawLine(xCurrent, yOrig, xCurrent, y);
        g.drawLine(xAverage, yOrig, xAverage, y);
        g.drawLine(xMax, yOrig, xMax, y);
        g.drawLine(xTotal, yOrig, xTotal, y);
        g.drawLine(xColor, yOrig, xColor, y);
        g.setFont(oldFont);
    }
