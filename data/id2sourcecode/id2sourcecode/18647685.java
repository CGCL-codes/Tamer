    public static double pnorm(double q) {
        double up = 0.9999999;
        double lp = 0.0000001;
        while (Math.abs(up - lp) > 0.0001) {
            if (qnorm((up + lp) / 2) <= q) {
                lp = (up + lp) / 2;
            } else {
                up = (up + lp) / 2;
            }
        }
        return up;
    }
