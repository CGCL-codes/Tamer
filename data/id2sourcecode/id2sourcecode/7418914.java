    private static double binRoot(double x) {
        double eps = 0.0001;
        double lower = 0.0;
        double upper = x;
        double mid = 0.0;
        for (int i = 0; i < 100; i++) {
            mid = (lower + upper) / 2;
            double cur = mid * mid;
            if (Math.abs(x - cur) < eps) {
                return mid;
            }
            if (cur < x) lower = mid; else upper = mid;
        }
        return mid;
    }
