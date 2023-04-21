    public static double qtrng(double p, double v, double r) {
        final int jmax = 8;
        final double pcut = 0.001;
        final double eps = 1e-4;
        if (v < 1) {
            throw new IllegalArgumentException("Degrees of freedom must be >= 1.");
        }
        if (r < 2) {
            throw new IllegalArgumentException("Number of samples must be >= 2.");
        }
        if (p < 0.9 || p > 0.99) {
            throw new IllegalArgumentException("P-value must be in range [0.9,0.99].");
        }
        double q1 = qtrng0(p, v, r);
        double p1 = prtrng(q1, v, r);
        double q2 = 0;
        double p2 = 0;
        double qtrng = q1;
        if (Math.abs(p1 - p) < pcut) {
            return qtrng;
        }
        if (p1 > p) {
            p1 = 1.75 * p - 0.75 * p1;
        }
        if (p1 < p) {
            p2 = p + (p - p1) * (1 - p) / (1 - p1) * 0.75;
        }
        if (p2 < 0.8) {
            p2 = 0.8;
        }
        if (p2 > 0.995) {
            p2 = 0.995;
        }
        q2 = qtrng0(p2, v, r);
        double e1 = 0;
        double e2 = 0;
        double d = 0;
        for (int j = 2; j <= jmax; ++j) {
            p2 = prtrng(q2, v, r);
            e1 = p1 - p;
            e2 = p2 - p;
            qtrng = (q1 + q2) / 2;
            d = e2 - e1;
            if (Math.abs(d) > eps) {
                qtrng = (e2 * q1 - e1 * q2) / d;
            }
            if (Math.abs(e1) >= Math.abs(e2)) {
                q1 = q2;
                p1 = p2;
            }
            if (Math.abs(p1 - p) < pcut * 5) {
                return qtrng;
            }
            q2 = qtrng;
        }
        return qtrng;
    }
