    public static Point[] simplify(Point[] series, double degree) {
        if ((series == null) || (series.length < 3)) {
            return series;
        }
        double x0 = series[0].getX();
        double y0 = series[0].getY();
        double x1 = series[1].getX();
        double y1 = series[1].getY();
        double x2 = series[2].getX();
        double y2 = series[2].getY();
        double alpha0 = Math.atan((y1 - y0) / (x1 - x0));
        double alpha1 = Math.atan((y2 - y1) / (x2 - x1));
        Point[] out;
        if (Math.abs(alpha1 - alpha0) < degree) {
            out = new Point[series.length - 1];
            out[0] = series[0];
            for (int i = 1; i < out.length; i++) {
                out[i] = series[i + 1];
            }
            return simplify(out);
        } else {
            Point[] tmp = new Point[series.length - 1];
            for (int i = 0; i < series.length - 1; i++) {
                tmp[i] = series[i + 1];
            }
            tmp = simplify(tmp);
            out = new Point[1 + tmp.length];
            out[0] = series[0];
            for (int i = 1; i < out.length; i++) {
                out[i] = tmp[i - 1];
            }
        }
        return out;
    }
