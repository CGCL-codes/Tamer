    void drawEdge(Graphics g, FontMetrics fm, int x1, int y1, int x2, int y2, int c) {
        int a = x1 - x2;
        int b = y1 - y2;
        drawArrow(g, x1, y1, x2, y2);
        int w = fm.stringWidth("" + c);
        int h = fm.getHeight();
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        if ((b < 0) || ((b == 0) && (a < 0))) x += fm.stringWidth(" "); else x -= w + fm.stringWidth(" ");
        if ((a > 0) || ((a == 0) && (b < 0))) y += 1; else y -= h;
        g.setColor(getBackground());
        g.fillRect(x, y, w, h);
        if (c != 0) {
            g.setColor(Color.black);
            g.drawString("" + c, x, y + fm.getAscent());
        }
    }
