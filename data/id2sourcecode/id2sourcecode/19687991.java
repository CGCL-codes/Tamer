    private void evalUniform(int k0, int n, int i, double t, double[] r) {
        int j, k;
        if (n == 0) {
            r[0] = 1.0;
        } else {
            double[] rTmp = new double[r.length - 1];
            for (j = 0; j < r.length - 1; j++) {
                rTmp[j] = r[j + 1];
            }
            evalUniform(k0, (n - 1), (i + 1), t, rTmp);
            for (j = 0; j < r.length - 1; j++) {
                r[j + 1] = rTmp[j];
            }
            for (j = 0, k = i; j <= n; j++, k++) {
                if (j == 0) {
                    r[j] = (k0 + (k + n + 1) - t) * r[j + 1] / n;
                } else {
                    if (j == n) {
                        r[j] = (t - k0 + k) * r[j] / n;
                    } else {
                        r[j] = ((t - k0 + k) * r[j] + (k0 + (k + n + 1) - t) * r[j + 1]) / n;
                    }
                }
            }
        }
    }
