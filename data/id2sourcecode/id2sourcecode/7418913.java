    public static double findt(double conf, double nu) {
        double eps = 0.00001;
        double lower = 1.0;
        double upper = 100.0;
        double mid = 0.0;
        for (int i = 0; i < 100; i++) {
            mid = (lower + upper) / 2;
            double cur = tTest(mid, nu);
            if (Math.abs(conf - cur) < eps) {
                return mid;
            }
            if (cur < conf) lower = mid; else upper = mid;
        }
        return mid;
    }
