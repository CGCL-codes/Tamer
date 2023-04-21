    public void moveTo(double x, double y) {
        x1 = x - (x2 - x1) / 2;
        x2 = x + (x2 - x1) / 2;
        y1 = y - (y2 - y1) / 2;
        y2 = y + (y2 - y1) / 2;
    }
