    protected final double seval(int n, double u, double[] x, double[] y, double[] b, double[] c, double[] d) {
        if (n <= 1) return y[0];
        int i = 0;
        int j = n - 1;
        while (true) {
            int k = (i + j) / 2;
            if (u < x[k]) j = k; else i = k;
            if (j <= i + 1) {
                double dx = u - x[i];
                return (y[i] + dx * (b[i] + dx * (c[i] + dx * d[i])));
            }
        }
    }
