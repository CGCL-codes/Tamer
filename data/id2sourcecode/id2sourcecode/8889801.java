    private void paintAngleLine(Graphics g, DisplaySettings settings, int x1, int y1, int x2, int y2, int x3, int y3) {
        int xa = (x1 + x2) / 2;
        int ya = (y1 + y2) / 2;
        int xb = (x3 + x2) / 2;
        int yb = (y3 + y2) / 2;
        g.setColor(settings.getAngleColor());
        String vers = System.getProperty("java.version");
        if (vers.compareTo("1.2") >= 0) {
            Graphics2D g2 = (Graphics2D) g;
            BasicStroke dotted = new BasicStroke(1, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[] { 3, 3 }, 0);
            g2.setStroke(dotted);
            g2.drawLine(xa, ya, xb, yb);
        } else {
            g.drawLine(xa, ya, xb, yb);
        }
    }
