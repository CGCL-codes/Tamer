    public double[] zoomInOnPixel(int x, int y) {
        if (badData()) return getLimits();
        double halfwidth = (xmax - xmin) / 4.0;
        double halfheight = (ymax - ymin) / 4.0;
        if (Math.abs(halfheight) < 1e-100 || Math.abs(halfwidth) < 1e-100) return null;
        double xclick = pixelToX(x);
        double yclick = pixelToY(y);
        double centerx = (xmin + xmax) / 2;
        double centery = (ymin + ymax) / 2;
        double newCenterx = (centerx + xclick) / 2;
        double newCentery = (centery + yclick) / 2;
        setLimits(newCenterx - halfwidth, newCenterx + halfwidth, newCentery - halfheight, newCentery + halfheight);
        return getLimits();
    }
