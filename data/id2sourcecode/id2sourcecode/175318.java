    public void setWeightCenter() {
        double px = (x1 + x2) / 2;
        double py = (y1 + y2) / 2;
        this.weightCenterX = (x3 + 2 * px) / 3;
        this.weightCenterY = (y3 + 2 * py) / 3;
    }
