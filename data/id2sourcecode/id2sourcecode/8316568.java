    public static void computeSplineSlopes(int n, double x[], double y[], double s[]) {
        int i, j;
        double h[] = new double[n];
        double hinv[] = new double[n];
        double g[] = new double[n];
        double a[] = new double[n + 1];
        double b[] = new double[n + 1];
        double fac;
        for (i = 0; i < n; i++) {
            h[i] = x[i + 1] - x[i];
            hinv[i] = 1.0 / h[i];
            g[i] = 3 * (y[i + 1] - y[i]) * hinv[i] * hinv[i];
        }
        a[0] = 2 * hinv[0];
        b[0] = g[0];
        for (i = 1; i <= n; i++) {
            fac = hinv[i - 1] / a[i - 1];
            a[i] = (2 - fac) * hinv[i - 1];
            b[i] = g[i - 1] - fac * b[i - 1];
            if (i < n) {
                a[i] += 2 * hinv[i];
                b[i] += g[i];
            }
        }
        s[n] = b[n] / a[n];
        for (i = n - 1; i >= 0; i--) s[i] = (b[i] - hinv[i] * s[i + 1]) / a[i];
    }
