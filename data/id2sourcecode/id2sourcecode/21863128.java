    public double f(double t) {
        double[] w = ArrayUtils.copy(y);
        for (int i = 0; i < y.length; i++) {
            for (int j = i - 1; j >= 0; j--) w[j] = w[j + 1] + (w[j + 1] - w[j]) * (t - x[i]) / (x[i] - x[j]);
        }
        return w[0];
    }
