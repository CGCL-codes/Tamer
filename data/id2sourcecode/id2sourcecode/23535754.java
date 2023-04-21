    public PSpline(double[] x, double[] y) {
        n = x.length - 1;
        this.x = x;
        this.y = y;
        this.z = new double[n + 1];
        double[] h = new double[n + 1], d = new double[n + 1], w = new double[n + 1];
        for (int i = 0; i < n; i++) {
            h[i] = x[i + 1] - x[i];
            w[i] = (y[i + 1] - y[i]) / h[i];
        }
        w[n] = w[0];
        for (int i = 1; i < n; i++) d[i] = 2 * (x[i + 1] - x[i - 1]);
        d[n] = 2 * (h[n - 1] + h[0]);
        for (int i = 1; i <= n; i++) z[i] = w[i] - w[i - 1];
        w[1] = h[0];
        w[n - 1] = h[n - 1];
        w[n] = d[n];
        for (int i = 2; i < n - 1; i++) w[i] = 0;
        for (int i = 1; i < n; i++) {
            double t = h[i] / d[i];
            z[i + 1] -= z[i] * t;
            d[i + 1] -= h[i] * t;
            w[i + 1] -= w[i] * t;
        }
        w[0] = w[n];
        z[0] = z[n];
        for (int i = n - 2; i >= 0; i--) {
            double t = h[i] / d[i + 1];
            z[i] -= z[i + 1] * t;
            w[i] -= w[i + 1] * t;
        }
        double t = z[0] / w[0];
        z[0] = z[n] = t;
        for (int i = 1; i < n; i++) z[i] = (z[i] - w[i] * t) / d[i];
    }
