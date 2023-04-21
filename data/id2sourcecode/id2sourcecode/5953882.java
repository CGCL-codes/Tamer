    private void setShapeJiggle(double x1, double y1, double x2, double y2, boolean upd) {
        double s = 0;
        if (y2 == y1) {
            if (upd) {
                s = 1 / (1 / (x2 - x1));
            } else {
                s = 1 / (-1 / (x2 - x1));
            }
        } else {
            s = 1 / ((y2 - y1) / (x2 - x1));
        }
        double x3 = (x1 + x2) / 2;
        double y3 = (y1 + y2) / 2;
        double dist = Math.min(4, Point2D.distance(x1, y1, x2, y2) / 8);
        double a = s * s + 1;
        double b = -(2 + 2 * s * s) * x3;
        double c = x3 * x3 + s * s * x3 * x3 - dist * dist;
        double x41 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double x42 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        double y41 = y3 + s * (x3 - x41);
        double y42 = y3 + s * (x3 - x42);
        if (upd) {
            setShape(new QuadCurve2D.Double(x1, y1, x41, y41, x2, y2));
        } else {
            setShape(new QuadCurve2D.Double(x1, y1, x42, y42, x2, y2));
        }
    }
