    private void drawRelationText(OGraphics g, int x1, int y1, int x2, int y2) {
        int mx;
        int my;
        if (this.name != null) {
            mx = (x2 + x1) / 2;
            my = (y2 + y1) / 2;
            java.awt.FontMetrics fm = _owner.getFontMetrics(font);
            int rx = mx - 10;
            int ry = my + fm.getHeight();
            g.setColor(Color.blue);
            g.drawString(name, rx, ry);
            g.setColor(Color.black);
        }
    }
