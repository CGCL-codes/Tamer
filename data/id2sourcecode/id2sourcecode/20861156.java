    public static void draw(Graphics2D g, int pat, String label, Color c, int orientation, boolean back) {
        int x1 = (int) (Math.cos(Math.PI / 3 + (pat - 1) * Math.PI / 3) * edge + xc);
        int y1 = (int) (-Math.sin(Math.PI / 3 + (pat - 1) * Math.PI / 3) * edge + yc);
        int x2 = (int) (Math.cos(Math.PI / 3 + pat * Math.PI / 3) * edge + xc);
        int y2 = (int) (-Math.sin(Math.PI / 3 + pat * Math.PI / 3) * edge + yc);
        g.setColor(c);
        g.fillPolygon(new int[] { x1, x2, xc }, new int[] { y1, y2, yc }, 3);
        g.setColor(Color.cyan);
        g.drawLine(x1, y1, x2, y2);
        g.drawLine(x1, y1, xc, yc);
        g.drawLine(xc, yc, x2, y2);
        int cx = (x1 + x2 + xc) / 3;
        int cy = (y1 + y2 + yc) / 3;
        g.setColor(back ? Color.gray.brighter() : Color.cyan);
        g.fillArc(cx - radius / 2, cy - radius / 2, radius, radius, 0, 360);
        int xo;
        int yo;
        if (orientation == HexaFlexagon.PREVIOUS) {
            xo = (x1 + xc) / 2;
            yo = (y1 + yc) / 2;
        } else if (orientation == HexaFlexagon.NEXT) {
            xo = (xc + x2) / 2;
            yo = (yc + y2) / 2;
        } else {
            xo = (x1 + x2) / 2;
            yo = (y1 + y2) / 2;
        }
        g.setStroke(new BasicStroke(4));
        g.drawLine(xo, yo, cx, cy);
        g.setColor(back ? Color.red : Color.black);
        Font f = g.getFont().deriveFont(Font.BOLD);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        int dy = fm.getAscent();
        int dx = fm.stringWidth(label);
        g.drawString(label, cx - dx / 2, cy + dy / 2);
    }
