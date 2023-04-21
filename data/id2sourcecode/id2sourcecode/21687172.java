    private final void seval2(int n, double u, double[] x, double[] y, double[] b, double[] c, double[] d, double[] outputs) {
        if (n <= 1) {
            outputs[0] = y[0];
            outputs[1] = 0;
            return;
        }
        int i = 0;
        int j = n - 1;
        while (true) {
            int k = (i + j) / 2;
            if (u < x[k]) j = k; else i = k;
            if (j <= i + 1) {
                double dx = u - x[i];
                outputs[0] = y[i] + dx * (b[i] + dx * (c[i] + dx * d[i]));
                outputs[1] = b[i] + dx * (c[i] + c[i] + dx * (d[i] + d[i] + d[i]));
                return;
            }
        }
    }
