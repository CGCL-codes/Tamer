    public static double quantile(double val, double[] quantiles) {
        int x1 = 1;
        int x2 = quantiles.length;
        int i = x2 / 2;
        while (x1 < x2) {
            if (quantiles[i] == val) {
                break;
            } else if (quantiles[i] < val) {
                x1 = i + 1;
            } else {
                x2 = i;
            }
            i = x1 + (x2 - x1) / 2;
        }
        return ((double) i) / (quantiles.length - 1);
    }
