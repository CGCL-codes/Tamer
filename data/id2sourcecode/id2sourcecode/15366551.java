    public static int pointCrossingsForQuad(double px, double py, double x0, double y0, double xc, double yc, double x1, double y1, int level) {
        if (py < y0 && py < yc && py < y1) return 0;
        if (py >= y0 && py >= yc && py >= y1) return 0;
        if (px >= x0 && px >= xc && px >= x1) return 0;
        if (px < x0 && px < xc && px < x1) {
            if (py >= y0) {
                if (py < y1) return 1;
            } else {
                if (py >= y1) return -1;
            }
            return 0;
        }
        if (level > 52) return pointCrossingsForLine(px, py, x0, y0, x1, y1);
        double x0c = (x0 + xc) / 2;
        double y0c = (y0 + yc) / 2;
        double xc1 = (xc + x1) / 2;
        double yc1 = (yc + y1) / 2;
        xc = (x0c + xc1) / 2;
        yc = (y0c + yc1) / 2;
        if (Double.isNaN(xc) || Double.isNaN(yc)) {
            return 0;
        }
        return (pointCrossingsForQuad(px, py, x0, y0, x0c, y0c, xc, yc, level + 1) + pointCrossingsForQuad(px, py, xc, yc, xc1, yc1, x1, y1, level + 1));
    }
