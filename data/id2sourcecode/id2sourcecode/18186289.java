    private void paintTriangleWest(Graphics g, int x, int y, int size, boolean isEnabled) {
        int tipX = x;
        int tipY = y + (size - 2) / 2;
        int baseX = x + (size - 1);
        int baseY1 = tipY - (size - 1);
        int baseY2 = tipY + (size - 1);
        Polygon triangle = new Polygon();
        triangle.addPoint(tipX, tipY);
        triangle.addPoint(baseX, baseY1);
        triangle.addPoint(baseX, baseY2);
        if (isEnabled) {
            g.setColor(Color.DARK_GRAY);
            g.fillPolygon(triangle);
            g.drawPolygon(triangle);
        } else {
            g.setColor(Color.GRAY);
            g.fillPolygon(triangle);
            g.drawPolygon(triangle);
            g.setColor(Color.WHITE);
            g.drawLine(baseX + 1, baseY1 + 1, baseX + 1, baseY2 + 1);
        }
    }
