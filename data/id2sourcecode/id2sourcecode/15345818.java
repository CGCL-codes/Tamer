    private void renderHalo() {
        int diameter = (width1 + width2 + 1) / 2;
        int x = (x1 + x2) / 2, y = (y1 + y2) / 2;
        int halowidth = diameter / 4;
        if (halowidth < 4) halowidth = 4;
        if (halowidth > 10) halowidth = 10;
        int halodiameter = diameter + 2 * halowidth;
        int haloradius = (halodiameter + 1) / 2;
        g.setColor(colorSelection);
        g.fillOval(x - haloradius, y - haloradius, halodiameter, halodiameter);
    }
