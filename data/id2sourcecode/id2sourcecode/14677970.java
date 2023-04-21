    public double[] equalizeAxes() {
        if (badData()) return getLimits();
        double w = xmax - xmin;
        double h = ymax - ymin;
        double pixelWidth = w / (width - 2 * gap - 1);
        double pixelHeight = h / (height - 2 * gap - 1);
        double newXmin, newXmax, newYmin, newYmax;
        if (pixelWidth < pixelHeight) {
            double centerx = (xmax + xmin) / 2;
            double halfwidth = w / 2 * pixelHeight / pixelWidth;
            newXmax = centerx + halfwidth;
            newXmin = centerx - halfwidth;
            newYmin = ymin;
            newYmax = ymax;
        } else if (pixelWidth > pixelHeight) {
            double centery = (ymax + ymin) / 2;
            double halfheight = h / 2 * pixelWidth / pixelHeight;
            newYmax = centery + halfheight;
            newYmin = centery - halfheight;
            newXmin = xmin;
            newXmax = xmax;
        } else return null;
        setLimits(newXmin, newXmax, newYmin, newYmax);
        return getLimits();
    }
