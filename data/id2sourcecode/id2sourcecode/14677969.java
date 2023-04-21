    public double[] zoomOutFromPixel(int x, int y) {
        if (badData()) return getLimits();
        double halfwidth = (xmax - xmin);
        double halfheight = (ymax - ymin);
        if (Math.abs(halfwidth) > 1e100 || Math.abs(halfheight) > 1e100) return null;
        double xclick = pixelToX(x);
        double yclick = pixelToY(y);
        double centerx = (xmin + xmax) / 2;
        double centery = (ymin + ymax) / 2;
        double newCenterx = 2 * centerx - xclick;
        double newCentery = 2 * centery - yclick;
        setLimits(newCenterx - halfwidth, newCenterx + halfwidth, newCentery - halfheight, newCentery + halfheight);
        return getLimits();
    }
