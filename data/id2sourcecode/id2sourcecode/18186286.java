    private void paintTriangleNorth(Graphics g, int x, int y, int size, boolean isEnabled) {
        int tipX = x + (size - 2) / 2;
        int tipY = y;
        int baseX1 = tipX - (size - 1);
        int baseX2 = tipX + (size - 1);
        int baseY = y + (size - 1);
        Polygon triangle = new Polygon();
        triangle.addPoint(tipX, tipY);
        triangle.addPoint(baseX1, baseY);
        triangle.addPoint(baseX2, baseY);
        if (isEnabled) {
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(triangle);
            g.drawPolygon(triangle);
        } else {
            g.setColor(Color.GRAY);
            g.fillPolygon(triangle);
            g.drawPolygon(triangle);
            g.setColor(Color.WHITE);
            g.drawLine(baseX1 + 1, baseY + 1, baseX2 + 1, baseY + 1);
        }
    }
