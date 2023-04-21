    double intersection(double x1, double y1, double x2, double y2) {
        if (x1 * x1 + y1 * y1 > (R - t - f) * (R - t - f)) {
            return 0;
        }
        if (x2 * x2 + y2 * y2 < (R - t - f) * (R - t - f)) {
            return (x2 - x1) * (y2 - y1);
        }
        if ((x2 - x1) * (y2 - y1) < (R - t - f) * (R - t - f) * 0.000000000001) {
            return (x2 - x1) * (y2 - y1) / 2;
        }
        double mx = (x1 + x2) / 2;
        double my = (y1 + y2) / 2;
        return intersection(x1, y1, mx, my) + intersection(mx, y1, x2, my) + intersection(x1, my, mx, y2) + intersection(mx, my, x2, y2);
    }
