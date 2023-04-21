    protected final void spline(int n, double[] x, double[] y, double[] b, double[] c, double[] d) {
        if (n < 3) {
            b[0] = 0;
            if (n == 2) b[0] = (y[1] - y[0]) / (x[1] - x[0]);
            c[0] = 0;
            d[0] = 0;
            b[1] = b[0];
            c[1] = 0;
            d[1] = 0;
            return;
        }
        d[0] = x[1] - x[0];
        c[1] = (y[1] - y[0]) / d[0];
        for (int i = 1; i < n - 1; ++i) {
            d[i] = x[i + 1] - x[i];
            b[i] = 2.0 * (d[i - 1] + d[i]);
            c[i + 1] = (y[i + 1] - y[i]) / d[i];
            c[i] = c[i + 1] - c[i];
        }
        b[0] = -d[0];
        b[n - 1] = -d[n - 2];
        c[0] = 0.0;
        c[n - 1] = 0.0;
        if (n > 3) {
            c[0] = c[2] / (x[3] - x[1]) - c[1] / (x[2] - x[0]);
            c[n - 1] = c[n - 2] / (x[n - 1] - x[n - 3]) - c[n - 3] / (x[n - 2] - x[n - 4]);
            c[0] = c[0] * d[0] * d[0] / (x[3] - x[0]);
            c[n - 1] = -c[n - 1] * d[n - 2] * d[n - 2] / (x[n - 1] - x[n - 4]);
        }
        for (int i = 1; i < n; ++i) {
            double t = d[i - 1] / b[i - 1];
            b[i] = b[i] - t * d[i - 1];
            c[i] = c[i] - t * c[i - 1];
        }
        c[n - 1] = c[n - 1] / b[n - 1];
        for (int i = n - 2; i >= 0; --i) {
            c[i] = (c[i] - d[i] * c[i + 1]) / b[i];
        }
        b[n - 1] = (y[n - 1] - y[n - 2]) / d[n - 2] + d[n - 2] * (c[n - 2] + c[n - 1] + c[n - 1]);
        for (int i = 0; i < n - 1; ++i) {
            b[i] = (y[i + 1] - y[i]) / d[i] - d[i] * (c[i + 1] + c[i] + c[i]);
            d[i] = (c[i + 1] - c[i]) / d[i];
            c[i] = 3.0 * c[i];
        }
        c[n - 1] = 3.0 * c[n - 1];
        d[n - 1] = d[n - 2];
    }
