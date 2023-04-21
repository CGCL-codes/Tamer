    private void drawWire(JMachine b1, JMachine b2, Graphics g, double arrowRadius, JWire aWire) {
        int x1 = b1.getX() + b1.getWidth() / 2;
        int y1 = b1.getY() + b1.getHeight() / 2;
        int x2 = b2.getX() + b2.getWidth() / 2;
        int y2 = b2.getY() + b2.getHeight() / 2;
        g.setColor(getForeground());
        g.drawLine(x1, y1, x2, y2);
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        int dx = x2 - x1;
        int dy = y2 - y1;
        double angle = 0.0;
        angle = Math.atan2(dy, dx);
        int px1 = (int) (cx + Math.cos(angle) * arrowRadius);
        int py1 = (int) (cy + Math.sin(angle) * arrowRadius);
        int px2 = (int) (cx + Math.cos(angle + DEG120) * arrowRadius);
        int py2 = (int) (cy + Math.sin(angle + DEG120) * arrowRadius);
        int px3 = (int) (cx + Math.cos(angle - DEG120) * arrowRadius);
        int py3 = (int) (cy + Math.sin(angle - DEG120) * arrowRadius);
        Polygon p = new Polygon(new int[] { px1, px2, px3 }, new int[] { py1, py2, py3 }, 3);
        g.setColor(wireColors[aWire.wire.channel]);
        g.fillPolygon(p);
        aWire.poly = p;
    }
