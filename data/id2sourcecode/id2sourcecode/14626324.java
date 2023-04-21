    public double[] getClosestPoint(double x, double y) {
        double point[] = { 0.0, 0.0, 0.0 };
        int i;
        double xdiff, ydiff, dist2;
        xdiff = data[0] - x;
        ydiff = data[1] - y;
        point[0] = data[0];
        point[1] = data[1];
        point[2] = xdiff * xdiff + ydiff * ydiff;
        for (i = stride; i < length - 1; i += stride) {
            xdiff = data[i] - x;
            ydiff = data[i + 1] - y;
            dist2 = xdiff * xdiff + ydiff * ydiff;
            if (dist2 < point[2]) {
                point[0] = data[i];
                point[1] = data[i + 1];
                point[2] = dist2;
            }
        }
        return point;
    }
