    public RegionImpl scale(double scale) {
        double oldHeight = end - start;
        double mid = (start + end) / 2;
        double newHeight = oldHeight * scale;
        this.start = mid - newHeight / 2;
        this.end = mid + newHeight / 2;
        return this;
    }
