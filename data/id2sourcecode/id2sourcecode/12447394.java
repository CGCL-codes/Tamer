    public static int pointCrossingsForCubic(double px, double py, double x0, double y0, double xc0, double yc0, double xc1, double yc1, double x1, double y1, int level) {
        if (py < y0 && py < yc0 && py < yc1 && py < y1) return 0;
        if (py >= y0 && py >= yc0 && py >= yc1 && py >= y1) return 0;
        if (px >= x0 && px >= xc0 && px >= xc1 && px >= x1) return 0;
        if (px < x0 && px < xc0 && px < xc1 && px < x1) {
            if (py >= y0) {
                if (py < y1) return 1;
            } else {
                if (py >= y1) return -1;
            }
            return 0;
        }
        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double xmid = (xc0 + xc1) / 2;
        double ymid = (yc0 + yc1) / 2;
        xc0 = (x0 + xc0) / 2;
        yc0 = (y0 + yc0) / 2;
        xc1 = (xc1 + x1) / 2;
        yc1 = (yc1 + y1) / 2;
        double xc0m = (xc0 + xmid) / 2;
        double yc0m = (yc0 + ymid) / 2;
        double xmc1 = (xmid + xc1) / 2;
        double ymc1 = (ymid + yc1) / 2;
        xmid = (xc0m + xmc1) / 2;
        ymid = (yc0m + ymc1) / 2;
        if (Double.isNaN(xmid) || Double.isNaN(ymid)) {
            return 0;
        }
        return (pointCrossingsForCubic(px, py, x0, y0, xc0, yc0, xc0m, yc0m, xmid, ymid, level + 1) + pointCrossingsForCubic(px, py, xmid, ymid, xmc1, ymc1, xc1, yc1, x1, y1, level + 1));
    }
