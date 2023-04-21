    private double linearApproximate(double[] x, double[] y, double pt, double low, double high) {
        int i = 0;
        int j = x.length - 1;
        if (pt < x[i]) {
            return low;
        }
        if (pt > x[j]) {
            return high;
        }
        while (i < j - 1) {
            int ij = (i + j) / 2;
            if (pt < x[ij]) {
                j = ij;
            } else {
                i = ij;
            }
        }
        if (pt == x[j]) {
            return y[j];
        }
        if (pt == x[i]) {
            return y[i];
        }
        return y[i] + (y[j] - y[i]) * ((pt - x[i]) / (x[j] - x[i]));
    }
