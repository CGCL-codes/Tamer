    private void evalNonUniform(int n, int i, double t, double[] r, double pTol) {
        double tk;
        double w1 = 0.0;
        double w2 = 0.0;
        int j, k;
        if (n == 0) {
            r[0] = 1.0;
        } else {
            double[] rTmp = new double[r.length - 1];
            for (j = 0; j < r.length - 1; j++) {
                rTmp[j] = r[j + 1];
            }
            evalNonUniform((n - 1), (i + 1), t, rTmp, pTol);
            for (j = 0; j < r.length - 1; j++) {
                r[j + 1] = rTmp[j];
            }
            for (j = 0, k = i; j <= n; j++, k++) {
                if (j != 0) {
                    if ((w1 = knotValueAt(k + n) - (tk = knotValueAt(k))) < pTol) {
                        w1 = 0.0;
                    } else {
                        w1 = (t - tk) / w1;
                    }
                }
                if (j != n) {
                    if ((w2 = (tk = knotValueAt(k + n + 1)) - knotValueAt(k + 1)) < pTol) {
                        w2 = 0.0;
                    } else {
                        w2 = (tk - t) / w2;
                    }
                }
                if (j == 0) {
                    r[j] = w2 * r[j + 1];
                } else {
                    if (j == n) {
                        r[j] = w1 * r[j];
                    } else {
                        r[j] = w1 * r[j] + w2 * r[j + 1];
                    }
                }
            }
        }
    }
