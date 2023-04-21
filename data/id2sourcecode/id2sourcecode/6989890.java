    protected void computeCoefficients() {
        final int n = degree() + 1;
        coefficients = new double[n];
        for (int i = 0; i < n; i++) {
            coefficients[i] = 0.0;
        }
        final double[] c = new double[n + 1];
        c[0] = 1.0;
        for (int i = 0; i < n; i++) {
            for (int j = i; j > 0; j--) {
                c[j] = c[j - 1] - c[j] * x[i];
            }
            c[0] *= -x[i];
            c[i + 1] = 1;
        }
        final double[] tc = new double[n];
        for (int i = 0; i < n; i++) {
            double d = 1;
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    d *= x[i] - x[j];
                }
            }
            final double t = y[i] / d;
            tc[n - 1] = c[n];
            coefficients[n - 1] += t * tc[n - 1];
            for (int j = n - 2; j >= 0; j--) {
                tc[j] = c[j + 1] + tc[j + 1] * x[i];
                coefficients[j] += t * tc[j];
            }
        }
        coefficientsComputed = true;
    }
