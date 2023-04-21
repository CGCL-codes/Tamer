    public Spline(double[] x, double[] y) {
        int n = x.length;
        this.x = x;
        this.y = y;
        z = new double[n];
        double[] h = new double[n], d = new double[n];
        z[0] = z[n - 1] = 0;
        for (int i = 0; i < n - 1; i++) {
            h[i] = x[i + 1] - x[i];
            d[i + 1] = (y[i + 1] - y[i]) / h[i];
        }
        z[1] = d[2] - d[1] - h[0] * z[0];
        d[1] = 2 * (x[2] - x[0]);
        for (int i = 1; i < n - 2; i++) {
            double t = h[i] / d[i];
            z[i + 1] = d[i + 2] - d[i + 1] - z[i] * t;
            d[i + 1] = 2 * (x[i + 2] - x[i]) - h[i] * t;
        }
        z[n - 2] -= h[n - 2] * z[n - 1];
        for (int i = n - 2; i > 0; i--) z[i] = (z[i] - h[i] * z[i + 1]) / d[i];
    }
