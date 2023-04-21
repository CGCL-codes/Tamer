    public static final Point calculateControlPoint(int x_source, int y_source, int x_target, int y_target, int lineStyle) {
        Point result = null;
        int x_diff = 0;
        int y_diff = 0;
        double x_ortho = 0;
        double y_ortho = 0;
        int x_middle = 0;
        double c = 0.2;
        if (lineStyle == MMAX2Constants.XCURVE) {
            x_diff = x_target - x_source;
            y_diff = y_target - y_source;
            x_ortho = y_diff * c;
            y_ortho = -x_diff * c;
            x_middle = (x_source + x_target) / 2;
            result = new Point((int) (x_middle + x_ortho), (int) (x_middle + y_ortho));
        } else if (x_source < x_target && y_source < y_target) {
            if (lineStyle == MMAX2Constants.LCURVE) {
                result = new Point((int) x_target, (int) y_source);
            } else if (lineStyle == MMAX2Constants.RCURVE) {
                result = new Point((int) x_source, (int) y_target);
            }
        } else if (x_source > x_target && y_source < y_target) {
            if (lineStyle == MMAX2Constants.LCURVE) {
                result = new Point((int) x_source, (int) y_target);
            } else if (lineStyle == MMAX2Constants.RCURVE) {
                result = new Point((int) x_target, (int) y_source);
            }
        } else if (x_source > x_target && y_source > y_target) {
            if (lineStyle == MMAX2Constants.LCURVE) {
                result = new Point((int) x_target, (int) y_source);
            } else if (lineStyle == MMAX2Constants.RCURVE) {
                result = new Point((int) x_source, (int) y_target);
            }
        } else if (x_source < x_target && y_source > y_target) {
            if (lineStyle == MMAX2Constants.LCURVE) {
                result = new Point((int) x_source, (int) y_target);
            } else if (lineStyle == MMAX2Constants.RCURVE) {
                result = new Point((int) x_target, (int) y_source);
            }
        } else if (y_source == y_target) {
            if (x_target < x_source) {
                if (lineStyle == MMAX2Constants.LCURVE) {
                    result = new Point((int) ((int) x_source - (int) x_target) / 2 + (int) x_target, (int) y_source + 20);
                } else if (lineStyle == MMAX2Constants.RCURVE) {
                    result = new Point((int) ((int) x_source - (int) x_target) / 2 + (int) x_target, (int) y_source - 20);
                }
            } else {
                if (lineStyle == MMAX2Constants.LCURVE) {
                    result = new Point((int) ((int) x_target - (int) x_source) / 2 + (int) x_source, (int) y_source - 20);
                } else if (lineStyle == MMAX2Constants.RCURVE) {
                    result = new Point((int) ((int) x_target - (int) x_source) / 2 + (int) x_source, (int) y_source + 20);
                }
            }
        } else if (x_source == x_target) {
            if (y_target < y_source) {
                if (lineStyle == MMAX2Constants.LCURVE) {
                    result = new Point((int) x_source - 20, (int) ((int) y_source - (int) y_target) / 2 + (int) y_target);
                } else if (lineStyle == MMAX2Constants.RCURVE) {
                    result = new Point((int) x_source + 20, (int) ((int) y_source - (int) y_target) / 2 + (int) y_target);
                }
            } else {
                if (lineStyle == MMAX2Constants.LCURVE) {
                    result = new Point((int) x_source - 20, (int) ((int) y_target - (int) y_source) / 2 + (int) y_source);
                } else if (lineStyle == MMAX2Constants.RCURVE) {
                    result = new Point((int) x_source + 20, (int) ((int) y_target - (int) y_source) / 2 + (int) y_source);
                }
            }
        }
        return result;
    }
