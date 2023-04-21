    public double refine(double a, double b, double c, double target, double t) {
        if (t < -0.1 || t > 1.1) {
            return -1;
        }
        double y = YforT(t);
        double t0, t1;
        if (y < target) {
            t0 = t;
            t1 = 1;
        } else {
            t0 = 0;
            t1 = t;
        }
        boolean useslope = true;
        while (y != target) {
            if (!useslope) {
                double t2 = (t0 + t1) / 2;
                if (t2 == t0 || t2 == t1) {
                    break;
                }
                t = t2;
            } else {
                double slope = dYforT(t, 1);
                if (slope == 0) {
                    useslope = false;
                    continue;
                }
                double t2 = t + ((target - y) / slope);
                if (t2 == t || t2 <= t0 || t2 >= t1) {
                    useslope = false;
                    continue;
                }
                t = t2;
            }
            y = YforT(t);
            if (y < target) {
                t0 = t;
            } else if (y > target) {
                t1 = t;
            } else {
                break;
            }
        }
        return (t > 1) ? -1 : t;
    }
