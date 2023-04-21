    private void computeCoefficients() {
        int n = data.length;
        if (n <= 2) {
            coeffC = new float[n];
            return;
        }
        int n3 = n - 3;
        float[] h = new float[n];
        for (int i = 0; i < n - 1; i++) {
            h[i] = data[i + 1].x - data[i].x;
        }
        float[] r = new float[n3];
        float[] d = new float[n3 + 1];
        float[] l = new float[n3];
        float[] x = new float[n3 + 1];
        for (int i = 0; i <= n3; i++) {
            d[i] = 2 * (h[i] + h[i + 1]) - ((i == 0) ? 0 : r[i - 1] * l[i - 1]);
            if (i < n3) {
                r[i] = h[i + 1];
                l[i] = h[i + 1] / d[i];
            }
            x[i] = 3 * ((data[i + 2].y - data[i + 1].y) / h[i + 1] - (data[i + 1].y - data[i].y) / h[i]);
        }
        for (int i = 1; i <= n3; i++) {
            x[i] -= x[i - 1] * l[i - 1];
        }
        float[] coeff = new float[n];
        x[n3] /= d[n3];
        coeff[n3 + 1] = x[n3];
        for (int i = n3 - 1; i >= 0; i--) {
            x[i] = (x[i] - r[i] * x[i + 1]) / d[i];
            coeff[i + 1] = x[i];
        }
        coeffC = coeff;
    }
