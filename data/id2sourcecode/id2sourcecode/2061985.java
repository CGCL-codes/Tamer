    private void calcCoefficients() {
        a0 = new double[x.length - 1];
        a1 = new double[x.length - 1];
        a2 = new double[x.length - 1];
        a3 = new double[x.length - 1];
        b = new double[x.length];
        if (shape == LINEAR) {
            for (int i = 0; i < a0.length; i++) {
                double dx = x[i + 1] - x[i];
                if (dx == 0.0) continue;
                a1[i] = (y[i + 1] - y[i]) / dx;
                a0[i] = y[i] - a1[i] * x[i];
                a1[i] *= 0.5;
                b[i + 1] = b[i] + x[i + 1] * (a0[i] + x[i + 1] * a1[i]) - x[i] * (a0[i] + x[i] * a1[i]);
            }
            for (int i = 1; i < b.length - 1; i++) b[i] -= x[i] * (a0[i] + x[i] * a1[i]);
            return;
        }
        double m[][] = new double[4][4], a[] = new double[4], deriv[] = new double[x.length];
        for (int i = 1; i < x.length - 1; i++) if (x[i - 1] != x[i + 1]) deriv[i] = (y[i + 1] - y[i - 1]) / (x[i + 1] - x[i]);
        if (repeat) deriv[0] = deriv[x.length - 1] = (y[1] - y[y.length - 2]) / (1.0 + x[1] - x[x.length - 2]);
        for (int i = 0; i < a0.length; i++) {
            m[0][0] = 0.0;
            m[0][1] = 1.0;
            m[0][2] = 2.0 * x[i];
            m[0][3] = 3.0 * x[i] * x[i];
            a[0] = deriv[i];
            m[1][0] = 1.0;
            m[1][1] = x[i];
            m[1][2] = x[i] * x[i];
            m[1][3] = x[i] * x[i] * x[i];
            a[1] = y[i];
            m[2][0] = 1.0;
            m[2][1] = x[i + 1];
            m[2][2] = x[i + 1] * x[i + 1];
            m[2][3] = x[i + 1] * x[i + 1] * x[i + 1];
            a[2] = y[i + 1];
            m[3][0] = 0.0;
            m[3][1] = 1.0;
            m[3][2] = 2.0 * x[i + 1];
            m[3][3] = 3.0 * x[i + 1] * x[i + 1];
            a[3] = deriv[i + 1];
            SVD.solve(m, a);
            a0[i] = a[0];
            a1[i] = 0.5 * a[1];
            a2[i] = a[2] / 3.0;
            a3[i] = 0.25 * a[3];
            b[i + 1] = b[i] + x[i + 1] * (a0[i] + x[i + 1] * (a1[i] + x[i + 1] * (a2[i] + x[i + 1] * a3[i]))) - x[i] * (a0[i] + x[i] * (a1[i] + x[i] * (a2[i] + x[i] * a3[i])));
        }
        for (int i = 1; i < b.length - 1; i++) b[i] -= x[i] * (a0[i] + x[i] * (a1[i] + x[i] * (a2[i] + x[i] * a3[i])));
    }
