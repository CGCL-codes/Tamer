    private void renderHalo() {
        int halowidth = diameter / 4;
        if (halowidth < 4) halowidth = 4;
        if (halowidth > 10) halowidth = 10;
        int halodiameter = diameter + 2 * halowidth;
        int haloradius = (halodiameter + 1) / 2;
        g.setColor(colorSelection);
        g.fillOval(x - haloradius, y - haloradius, halodiameter, halodiameter);
    }
