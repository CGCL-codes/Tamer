    public ScreenTransform(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.xRange = maxX - minX;
        this.yRange = maxY - minY;
        this.centreX = (minX + maxX) / 2;
        this.centreY = (minY + maxY) / 2;
        this.zoom = 1;
        fixedAspectRatio = true;
    }
