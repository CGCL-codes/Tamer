    static Vector driving(int n) {
        double[] m = new double[n + 1];
        double[] k = new double[n + 1];
        m[1] = -c[0] / b[0];
        k[1] = d[0] / b[0];
        for (int i = 1; i < n; i++) {
            m[i + 1] = -c[i] / (a[i] * m[i] + b[i]);
            k[i + 1] = (d[i] - a[i] * k[i]) / (a[i] * m[i] + b[i]);
        }
        double[] y = new double[n + 1];
        y[n] = (d[n] - a[n] * k[n]) / (a[n] * m[n] + b[n]);
        for (int i = n - 1; i >= 0; i--) {
            y[i] = m[i + 1] * y[i + 1] + k[i + 1];
        }
        return new Vector(y);
    }
