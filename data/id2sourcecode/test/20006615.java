    public static double bisection(final Function f, double x1, double x2, final double tol) {
        int count = 0;
        int maxCount = (int) (Math.log(Math.abs(x2 - x1) / tol) / Math.log(2));
        maxCount = Math.max(MAX_ITERATIONS, maxCount) + 2;
        double y1 = f.evaluate(x1), y2 = f.evaluate(x2);
        if (y1 * y2 > 0) {
            NumericsLog.fine(count + " bisection root - interval endpoints must have opposite sign");
            return Double.NaN;
        }
        while (count < maxCount) {
            double x = (x1 + x2) / 2;
            double y = f.evaluate(x);
            if (Util.relativePrecision(Math.abs(x1 - x2), x) < tol) {
                return x;
            }
            if (y * y1 > 0) {
                x1 = x;
                y1 = y;
            } else {
                x2 = x;
                y2 = y;
            }
            count++;
        }
        NumericsLog.fine(count + " bisection root trials made - no convergence achieved");
        return Double.NaN;
    }
