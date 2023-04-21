    private static double[] maketable(double[] x, double[] y, int n) {
        double[] a = new double[n];
        double[] h = new double[n];
        double[] d = new double[n];
        int n_1 = n - 1;
        int n_2 = n - 2;
        a[0] = 0;
        a[n_1] = 0;
        for (int i = 0; i < n_1; i++) {
            h[i] = x[i + 1] - x[i];
            d[i + 1] = (y[i + 1] - y[i]) / h[i];
        }
        a[1] = d[2] - d[1] - h[0] * a[0];
        d[1] = 2 * (x[2] - x[0]);
        for (int i = 1; i < n_2; i++) {
            double t = h[i] / d[i];
            a[i + 1] = d[i + 2] - d[i + 1] - a[i] * t;
            d[i + 1] = 2 * (x[i + 2] - x[i]) - h[i] * t;
        }
        a[n_2] -= h[n_2] * a[n_1];
        for (int i = n_2; i > 0; i--) {
            a[i] = (a[i] - h[i] * a[i + 1]) / d[i];
        }
        return a;
    }
