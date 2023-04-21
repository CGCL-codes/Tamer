    public static double pchisq(double q, int df) {
        double up = 0.9999999;
        double lp = 0.0000001;
        while (Math.abs(up - lp) > 0.0001) {
            if (qchisq((up + lp) / 2, df) <= q) {
                lp = (up + lp) / 2;
            } else {
                up = (up + lp) / 2;
            }
        }
        return up;
    }
