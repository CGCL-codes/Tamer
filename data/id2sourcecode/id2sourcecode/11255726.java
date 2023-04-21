    public int getRadius() {
        if ((startAngle == 0 && stopAngle == 360) || (startAngle == stopAngle)) return cellRadius;
        int start = startAngle;
        int stop = stopAngle;
        radius = (outerRadius + innerRadius) / 2;
        if (start > stop) stop += 360;
        double angle = (double) ((stop - start) / 2);
        double r = Math.tan(Math.toRadians(angle)) * radius;
        return (int) r;
    }
