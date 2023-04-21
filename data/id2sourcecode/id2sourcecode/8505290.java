    public void paint(Graphics g) {
        int x, y;
        int top = vertSpace;
        int bottom = getSize().height - vertSpace;
        int left = horzSpace;
        int right = getSize().width - horzSpace;
        int width = right - left;
        int fullHeight = bottom - top;
        int centre = (top + bottom) / 2;
        int xAxisPos = centre;
        int yHeight = fullHeight / 2;
        if (plotStyle == SPECTRUM) {
            xAxisPos = bottom;
            yHeight = fullHeight;
        }
        this.setBackground(bgColor);
        if (logScale) {
            xAxisPos = top;
            g.setColor(gridColor);
            for (int i = 0; i <= vertIntervals; i++) {
                x = left + i * width / vertIntervals;
                g.drawLine(x, top, x, bottom);
            }
            for (int i = 0; i <= horzIntervals; i++) {
                y = top + i * fullHeight / horzIntervals;
                g.drawLine(left, y, right, y);
            }
        }
        g.setColor(axisColor);
        g.drawLine(left, top, left, bottom);
        g.drawLine(left, xAxisPos, right, xAxisPos);
        if (nPoints != 0) {
            g.setColor(plotColor);
            xScale = width / (float) (nPoints - 1);
            yScale = yHeight / ymax;
            int[] xCoords = new int[nPoints];
            int[] yCoords = new int[nPoints];
            for (int i = 0; i < nPoints; i++) {
                xCoords[i] = (int) (left + Math.round(i * xScale));
                yCoords[i] = (int) (xAxisPos - Math.round(plotValues[i] * yScale));
            }
            if (tracePlot) for (int i = 0; i < nPoints - 1; i++) g.drawLine(xCoords[i], yCoords[i], xCoords[i + 1], yCoords[i + 1]); else {
                for (int i = 0; i < nPoints; i++) g.drawLine(xCoords[i], xAxisPos, xCoords[i], yCoords[i]);
            }
        }
    }
