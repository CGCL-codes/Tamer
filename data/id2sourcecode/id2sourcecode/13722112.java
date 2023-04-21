    public void paintComponentOld(Graphics gg) {
        Graphics2D g = (Graphics2D) gg;
        g.setColor(Color.gray);
        g.fillRect(0, 0, getWidth(), getHeight());
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int cellDim = panelWidth < panelHeight ? panelWidth / zoomRatio : panelHeight / zoomRatio;
        if ((cellDim & 1) == 0) cellDim--;
        int gridDim = cellDim * zoomRatio;
        Point mousePos = data.getMousePosition();
        BufferedImage zone = picker.createScreenCapture(new Rectangle(mousePos.x - zoomRatio / 2, mousePos.y - zoomRatio / 2, zoomRatio, zoomRatio));
        AffineTransform transform = g.getTransform();
        g.translate((getWidth() - gridDim) / 2, (getHeight() - gridDim) / 2);
        g.drawImage(zone, 0, 0, gridDim, gridDim, 0, 0, zoomRatio, zoomRatio, this);
        g.setColor(Color.black);
        g.drawLine(gridDim / 2, gridDim / 2 - 3, gridDim / 2, gridDim / 2 + 3);
        g.drawLine(gridDim / 2 - 3, gridDim / 2, gridDim / 2 + 3, gridDim / 2);
        g.setColor(Color.black);
        switch(overlay) {
            case GRID:
                for (int x = 0; x < zoomRatio + 1; x++) {
                    g.drawLine(x * cellDim, 0, x * cellDim, gridDim);
                }
                for (int y = 0; y < zoomRatio + 1; y++) {
                    g.drawLine(0, y * cellDim, gridDim, y * cellDim);
                }
                break;
            case SQUARE:
                for (int x = 0; x < zoomRatio; x++) {
                    for (int y = 0; y < zoomRatio; y++) {
                        g.drawRect(x * cellDim + 2, y * cellDim + 2, cellDim - 5, cellDim - 5);
                    }
                }
                break;
            case CROSS:
                g.drawLine(0, 0, 2, 0);
                g.drawLine(0, 0, 0, 2);
                g.drawLine(gridDim, 0, gridDim - 2, 0);
                g.drawLine(gridDim, 0, gridDim, 2);
                g.drawLine(gridDim, gridDim, gridDim - 2, gridDim);
                g.drawLine(gridDim, gridDim, gridDim, gridDim - 2);
                g.drawLine(0, gridDim, 2, gridDim);
                g.drawLine(0, gridDim, 0, gridDim - 2);
                for (int x = 1; x < zoomRatio; x++) {
                    g.drawLine(x * cellDim, 0, x * cellDim, 2);
                    g.drawLine(x * cellDim - 2, 0, x * cellDim + 2, 0);
                    g.drawLine(x * cellDim, gridDim, x * cellDim, gridDim - 2);
                    g.drawLine(x * cellDim - 2, gridDim, x * cellDim + 2, gridDim);
                    for (int y = 1; y < zoomRatio; y++) {
                        g.drawLine(x * cellDim, y * cellDim - 2, x * cellDim, y * cellDim + 2);
                        g.drawLine(x * cellDim - 2, y * cellDim, x * cellDim + 2, y * cellDim);
                    }
                }
                for (int y = 1; y < zoomRatio; y++) {
                    g.drawLine(0, y * cellDim - 2, 0, y * cellDim + 2);
                    g.drawLine(0, y * cellDim, 2, y * cellDim);
                    g.drawLine(gridDim, y * cellDim - 2, gridDim, y * cellDim + 2);
                    g.drawLine(gridDim, y * cellDim, gridDim - 2, y * cellDim);
                }
            case NONE:
            default:
        }
        g.setColor(Color.black);
        g.drawRect(0, 0, gridDim, gridDim);
        g.setTransform(transform);
    }
